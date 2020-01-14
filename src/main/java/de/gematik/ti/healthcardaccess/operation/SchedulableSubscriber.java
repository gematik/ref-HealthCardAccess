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

import de.gematik.ti.healthcardaccess.operation.scheduler.DefaultScheduler;
import de.gematik.ti.healthcardaccess.operation.scheduler.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * Implementation of Subscriber for Operation callback. Scheduler, onSuccess, and onError are injectable.
 *
 * @param <T> - Type of the Result object
 *
 */
public class SchedulableSubscriber<T> implements Subscriber<T> {

    final Scheduler scheduler;
    final Consumer<T> onSuccess;
    final Consumer<Throwable> onError;

    public SchedulableSubscriber(final Scheduler scheduler, final Consumer<T> onSuccess, final Consumer<Throwable> onError) {
        this.scheduler = scheduler == null ? DefaultScheduler.getInstance() : scheduler;
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    public void onSuccess(T value) {
        onSuccess.accept(value);
    }

    @Override
    public void onError(Throwable t) throws RuntimeException {
        onError.accept(t);
    }

    @Override
    public ExecutorService subscriberScheduler() {
        return scheduler.subscriberScheduler();
    }

    @Override
    public ExecutorService observerScheduler() {
        return scheduler.observerScheduler();
    }
}
