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

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;

import de.gematik.ti.healthcardaccess.operation.scheduler.TestScheduler;

/**
 * Integrated TestSubscriber that provides functionality for chaining assertions on a subscribed Operation.
 *
 * @param <T> - Type of the Result object
 *
 */
public class TestSubscriber<T> implements Subscriber<T> {
    private T value = null;
    private Throwable exception = null;

    @Override
    public void onSuccess(final T value) {
        this.value = value;
    }

    @Override
    public void onError(final Throwable t) throws RuntimeException {
        this.exception = t;
    }

    @Override
    public ExecutorService subscriberScheduler() {
        return TestScheduler.getInstance().subscriberScheduler();
    }

    @Override
    public ExecutorService observerScheduler() {
        return TestScheduler.getInstance().observerScheduler();
    }

    public final TestSubscriber<T> assertSuccess() {
        if (this.exception != null) {
            throw new AssertionError("Assertion of success failed. Exception:\n "
                    + exception.toString());
        }
        return this;
    }

    public final TestSubscriber<T> assertPredicate(final Predicate<T> predicate) {
        this.assertSuccess();
        if (!predicate.test(this.value)) {
            throw new AssertionError("Predicate failed: " + predicate.toString() + " Actual value was: " + (value == null ? "null" : value.toString()));
        }

        return this;
    }

    public final TestSubscriber<T> assertValue(final Consumer<T> predicate) {
        predicate.accept(this.value);

        return this;
    }

    public final TestSubscriber<T> assertValue(final T value) {
        return assertPredicate(t -> value.equals(t));
    }

    public final TestSubscriber<T> assertError(final Predicate<Throwable> predicate) {
        if (!predicate.test(this.exception)) {
            throw new AssertionError("Error predicate failed: " + predicate.toString());
        }
        return this;
    }

    public final TestSubscriber<T> assertError(final Throwable error) {
        return assertError(e -> error.equals(e));
    }

    public T get() {
        return value;
    }
}
