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

package de.gematik.ti.healthcardaccess.operation.executor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Executes all submitted tasks directly in the same thread as the caller.
 *
 */
public final class TrampolineExecutorService extends AbstractExecutorService {
    //volatile because can be viewed by other threads
    private volatile boolean terminated = false;

    /**
     * {@inheritDoc}
     * @see ExecutorService#shutdown()
     */
    @Override
    public void shutdown() {
        terminated = true;
    }

    /**
     * {@inheritDoc}
     * @see ExecutorService#isShutdown()
     */
    @Override
    public boolean isShutdown() {
        return terminated;
    }

    /**
     * {@inheritDoc}
     * @see ExecutorService#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        return terminated;
    }

    /**
     * {@inheritDoc}
     * @see ExecutorService#awaitTermination(long, TimeUnit)
     */
    @Override
    public boolean awaitTermination(final long theTimeout, final TimeUnit theUnit) throws InterruptedException {
        return true; // we never timeout on immediate trampoline scheduling (e.g. everything has already been executed when you get here)
    }

    /**
     * {@inheritDoc}
     * @see ExecutorService#shutdownNow()
     */
    @Override
    public List<Runnable> shutdownNow() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     * @see ExecutorService#execute(Runnable) 
     */
    @Override
    public void execute(final Runnable theCommand) {
        if (!terminated) {
            theCommand.run();
        } else {
            throw new RejectedExecutionException();
        }
    }
}
