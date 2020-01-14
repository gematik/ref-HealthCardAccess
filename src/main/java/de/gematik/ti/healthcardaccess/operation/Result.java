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

import java.util.function.Function;

/**
 *
 * @param <T> Type of the Result object
 *
 */
public interface Result<T> {

    /**
     * evaluate with the given CheckedSupplier
     * @param supplier - specific CheckedSupplier
     * @param <T> -  Type of the CheckedSupplier
     * @return Result Object from type T
     */
    static <T> Result<T> evaluate(CheckedSupplier<T> supplier) {
        try {
            T t = supplier.get();
            return success(t);
        } catch (Throwable exception) {
            return failure(exception);
        }
    }

    /**
     * create a Success Object with the given value
     *
     * @param value - Value for the Success Object
     * @param <T> - Type of the Value
     * @return Success Object
     */
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * create a Failure Object with the given throwable
     *
     * @param throwable - Throwable for the Failure Object
     * @param <T> - Throwable
     * @return Success Object
     */
    static <T> Result<T> failure(Throwable throwable) {
        return new Failure<>(throwable);
    }

    /**
     * Map with given CheckedFunction
     *
     * @param mapper - CheckedFunction
     * @param <R> - Result Type
     * @return Result Object with the Type R
     */
    default <R> Result<R> map(CheckedFunction<? super T, ? extends R> mapper) {
        return flatMap(t -> success(mapper.apply(t)));
    }

    /**
     * FlatMap with given CheckedFunction
     *
     * @param mapper - CheckedFunction
     * @param <R> - Result Type
     * @return Result Object with the Type R
     */
    default <R> Result<R> flatMap(CheckedFunction<? super T, ? extends Result<R>> mapper) {
        return fold(e -> failure(e), t -> mapper.apply(t));
    }

    /**
     *
     * @param onFailure - function to call if execution failure
     * @param onSuccess - function to call if execution success
     * @param <R> - Return type
     * @return
     */
    <R> R fold(Function<Throwable, R> onFailure, CheckedFunction<T, R> onSuccess);

    /**
     * Return the execution Result on success or null if failed
     *
     * @return Value or Null
     */
    default T getOrNull() {
        return fold(__ -> null, t -> t);
    }

    /**
     * Return the execution Result on success or the given value if failed
     *
     * @param value - value to return if execution failed
     * @return Result or given value
     */
    default T getOrValue(T value) {
        return fold(__ -> value, t -> t);
    }

    /**
     * Object if the execution is success
     * @param <T> Type of the Success object
     */
    class Success<T> implements Result<T> {

        private final T t;

        public Success(final T t) {
            this.t = t;
        }

        @Override
        public <R> R fold(final Function<Throwable, R> onFailure, final CheckedFunction<T, R> onSuccess) {
            try {
                return onSuccess.apply(t);
            } catch (Throwable exception) {
                return onFailure.apply(exception);
            }
        }
    }

    /**
     * Object if the execution is failed
     *
     * @param <T> Type of the Failed object
     */
    class Failure<T> implements Result<T> {

        private final Throwable throwable;

        public Failure(final Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public <R> R fold(final Function<Throwable, R> onFailure, final CheckedFunction<T, R> onSuccess) {
            return onFailure.apply(throwable);
        }
    }

}
