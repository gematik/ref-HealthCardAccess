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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * UnitFuture acts as a Constant/Fixed value Future implementation.
 *
 * @param <T> the value the Future returns upon get().
 *
 */
public final class UnitFuture<T> implements Future<T> {

    private final T value;

    public UnitFuture(final T value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * @see Future#cancel(boolean)
     */
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }

    /**
     * {@inheritDoc}
     * @see Future#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return false;
    }

    /**
     * {@inheritDoc}
     * @see Future#isDone()
     */
    @Override
    public boolean isDone() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @see Future#get()
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public T get() throws InterruptedException, ExecutionException {
        return value;
    }

    /**
     * {@inheritDoc}
     * @see Future#get(long, TimeUnit)
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Override
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return value;
    }
}
