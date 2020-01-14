/*
 * Copyright (c) 2020 gematik GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.gematik.ti.healthcardaccess.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * ResultOperation functional interface
 *
 * @param <T>
 *         - Type of the Result object
 *
 */
@FunctionalInterface
public interface ResultOperation<T> extends CheckedFunction<ExecutorService, Future<Result<T>>> {
    Logger LOG = LoggerFactory.getLogger(ResultOperation.class);

    static <T> ResultOperation<T> unitRo(T value) {
        return executorService -> new UnitFuture<>(Result.success(value));
    }

    static <T> ResultOperation<T> lazyUnitRo(Supplier<Result<T>> valueProvider) {
        return executorService -> executorService.submit(valueProvider::get);
    }

    /**
     * Returns a new ResultOperation<R> that contains the Result<R> which results from applying the provided {@link CheckedFunction<? super T, ? extends
     * ResultOperation <R>> mapper} to the current Result<T>.
     *
     * @param mapper
     * @param <R>
     *
     * @return ResultOperation<R>
     */
    default <R> ResultOperation<R> flatMap(CheckedFunction<? super T, ? extends ResultOperation<R>> mapper) {
        return executorService -> apply(executorService).get().map(mapper::apply).fold(err -> new UnitFuture<>(new Result.Failure(err)),
                iResultOperation -> iResultOperation.apply(executorService)
        );
    }

    /**
     * Returns a new ResultOperation<R> that contains the Result<R> which results from applying the provided {@link CheckedFunction<? super T, ? extends R>
     * mapper} to the current Result<T>.
     *
     * @param mapper
     * @param <R>
     *
     * @return ResultOperation<R>
     */
    default <R> ResultOperation<R> map(CheckedFunction<? super T, ? extends R> mapper) {
        return executorService -> new UnitFuture<>(apply(executorService).get().map(mapper::apply));
    }

    default <U, R> ResultOperation<R> zip(ResultOperation<U> other, BiFunction<T, U, R> mapper) {
        return flatMap(t -> other.map(u -> mapper.apply(t, u)));
    }

    /**
     * Validate the current Result contained in the ResultOperation with the provided {@link Function<? super T, ? extends Result<T>> validator}.
     *
     * @param validator
     *         Validator to validate the object wrapped into the result.
     *
     * @return
     */
    default ResultOperation<T> validate(Function<? super T, ? extends Result<T>> validator) {
        return executorService -> new UnitFuture<>(apply(executorService).get().flatMap(validator::apply));
    }

    /**
     * Schedule the operation and upstream-chain on the specified ExecutorService.
     *
     * @param executorService
     *         ExecutorService to scheduler the ResultOperation chain on
     *
     * @return ResultOperation<T>
     */
    default ResultOperation<T> scheduleOn(ExecutorService executorService) {
        return es -> apply(executorService);
    }

    /**
     * Subscribe to the operation and provide callback functions to get notified of the results.
     *
     * @param subscriber
     *         The collection of callback functions
     */
    default void subscribe(Subscriber<T> subscriber) {
        subscriber.subscriberScheduler().submit(() -> {
            try {
                apply(subscriber.subscriberScheduler())
                        .get()
                        .fold(
                                err -> {
                                    ((Runnable) () -> subscriber.observerScheduler().submit(() -> subscriber.onError(err))).run();
                                    return null;
                                },
                                v -> {
                                    ((Runnable) () -> subscriber.observerScheduler().submit(() -> subscriber.onSuccess(v))).run();
                                    return null;
                                }
                        );
            } catch (Throwable t) {
                LOG.error("Subscriber Error ", t);
                subscriber.observerScheduler().submit(() -> {
                    subscriber.onError(t);
                });
            }
        });
    }

    /**
     * Convenience method to subscribe.
     *
     * @param onSuccess
     *         callback on success
     * @param onError
     *         callback upon Error/Exception
     */
    default void subscribe(Consumer<T> onSuccess, CheckedFunction<Throwable, Void> onError) {
        subscribe(new Subscriber<T>() {
            @Override
            public void onSuccess(final T value) {
                onSuccess.accept(value);
            }

            @Override
            public void onError(final Throwable t) throws RuntimeException {
                try {
                    onError.apply(t);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Convenience when only an onSuccess is expected. Note: use is discouraged outside of tests
     *
     * @param onSuccess
     *         callback on success
     */
    default void subscribe(Consumer<T> onSuccess) {
        subscribe(onSuccess, t -> {
            LOG.error("Subscriber Error ", t);
            throw t;
        });
    }

    /**
     * Integrated TestSubscriber for ease-of-testing
     *
     * @return new TestSubscriber
     */
    default TestSubscriber<T> test() {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        subscribe(subscriber);
        return subscriber;
    }

}
