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

package de.gematik.ti.healthcardaccess.operation.scheduler;

import de.gematik.ti.healthcardaccess.operation.executor.TrampolineExecutorService;

import java.util.concurrent.ExecutorService;

/**
 * Provides ExecutorServices for ResultOperation io and observing subscriptions in Test.
 */
public class TestScheduler implements Scheduler {

    private static TestScheduler instance;

    public static TestScheduler getInstance() {
        if (instance == null) {
            instance = new TestScheduler();
        }
        return instance;
    }

    @Override
    public ExecutorService subscriberScheduler() {
        return new TrampolineExecutorService();
    }

    @Override
    public ExecutorService observerScheduler() {
        return new TrampolineExecutorService();
    }
}
