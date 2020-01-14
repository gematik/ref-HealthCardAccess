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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultOperationTest {

    private static final Logger LOG = LoggerFactory.getLogger(ResultOperationTest.class);

    private static final String COMPARE_STRING = "abc123";
    private static final String COMPARE_STRING_FAIL = "xyz789";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldExecuteTasksOfTaskChainOnDifferentThreads() throws InterruptedException {
        ExecutorService fixedThreadPool1 = Executors.newFixedThreadPool(1);
        ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(1);

        final long[] threadIdMain = { Thread.currentThread().getId() };
        final long[] threadId01 = { -1 };
        final long[] threadId02 = { -1 };

        ResultOperation<String> resultOperation01 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            LOG.trace("ResultOperation 1 on ThreadID:  " + Thread.currentThread().getId());
            threadId01[0] = Thread.currentThread().getId();
            return Result.success("");
        });

        ResultOperation<String> resultOperation02 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            LOG.trace("ResultOperation 2 on ThreadID:  " + Thread.currentThread().getId());
            threadId02[0] = Thread.currentThread().getId();
            return Result.success("");
        });

        resultOperation01
                .scheduleOn(fixedThreadPool1)
                .flatMap(__ -> resultOperation02.scheduleOn(fixedThreadPool2))
                .subscribe(___ -> {
                });

        Thread.sleep(1000);
        Assert.assertThat(Arrays.equals(threadIdMain, threadId01), is(false));
        Assert.assertThat(Arrays.equals(threadIdMain, threadId02), is(false));
    }

    @Ignore
    @Test
    public void shouldNotBlockMainThreadExecution() throws InterruptedException {
        ExecutorService fixedThreadPool1 = Executors.newFixedThreadPool(1);
        ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(1);

        final boolean[] ro2DoneExecuting = { false };

        ResultOperation<String> resultOperation01 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            LOG.trace("ResultOperation 1 on ThreadID:  " + Thread.currentThread().getId());
            return Result.success("");
        });

        ResultOperation<String> resultOperation02 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            LOG.trace("ResultOperation 2 on ThreadID:  " + Thread.currentThread().getId());
            TimeUnit.MILLISECONDS.sleep(1000);
            ro2DoneExecuting[0] = true;
            return Result.success("");
        });

        resultOperation01.scheduleOn(fixedThreadPool1)
                .flatMap(__ -> resultOperation02.scheduleOn(fixedThreadPool2))
                .subscribe(___ -> {
                });

        // assert that resultOperation02 is still running and uncompleted when executing next line in main thread
        Assert.assertThat(ro2DoneExecuting[0], is(equalTo(false)));

        // assert that resultOperation02 has finished executing after an additional waiting time of 2000 milliseconds
        TimeUnit.MILLISECONDS.sleep(2000);
        Assert.assertThat(ro2DoneExecuting[0], is(equalTo(true)));
    }

    @Test
    public void shouldValidateResultAsSuccess() throws InterruptedException {
        final boolean[] validatedAsSuccess = { false };

        ResultOperation<String> resultOperation01 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            return Result.success(COMPARE_STRING);
        });

        resultOperation01
                .validate(
                        str -> {
                            if (str.equals(COMPARE_STRING)) {
                                return Result.success(str);
                            } else {
                                return Result.failure(new Exception("Validation of string not successful."));
                            }
                        })
                .subscribe(str -> validatedAsSuccess[0] = true);

        TimeUnit.MILLISECONDS.sleep(500);
        Assert.assertThat(validatedAsSuccess[0], is(equalTo(true)));
    }

    @Test
    public void shouldValidateResultAsFailure() throws InterruptedException {
        final boolean[] validateAsFailure = { false };
        ResultOperation<String> resultOperation01 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            return Result.success(COMPARE_STRING);
        });

        resultOperation01
                .validate(
                        str -> {
                            if (str.equals(COMPARE_STRING_FAIL)) {
                                return Result.success(str);
                            } else {
                                return Result.failure(new Exception("Validation of string not successful."));
                            }
                        })
                .subscribe(
                        str -> {
                            // do nothing, expect to enter in failure branch
                        },
                        err -> {
                            validateAsFailure[0] = true;
                            return null;
                        });

        TimeUnit.MILLISECONDS.sleep(500);
        Assert.assertThat(validateAsFailure[0], is(equalTo(true)));
    }

    @Test
    public void shouldZipResultOperations() throws InterruptedException {
        final String[] result = { "overwriteMe" };

        ResultOperation<String> resultOperationString = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            return Result.success(COMPARE_STRING);
        });
        ResultOperation<Boolean> takeIt = executorService -> executorService.submit((Callable<Result<Boolean>>) () -> {
            return Result.success(true);
        });
        BiFunction<String, Boolean, String> takeOrEmptyFunc = (s, mayBe) -> mayBe ? s : "";

        resultOperationString
                .zip(takeIt, takeOrEmptyFunc)
                .subscribe(str -> result[0] = str);

        TimeUnit.MILLISECONDS.sleep(500);
        Assert.assertThat(result[0], is(equalTo(COMPARE_STRING)));
    }

    @Test
    public void mapOfSuccessShouldRenderSuccess() throws InterruptedException {
        boolean[] enteredSuccessBranch = { false };
        ResultOperation<String> resultOperation01 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            return Result.success(COMPARE_STRING);
        });

        resultOperation01
                .map(str -> str.toUpperCase())
                .subscribe(str -> enteredSuccessBranch[0] = true);

        TimeUnit.MILLISECONDS.sleep(1000);
        Assert.assertThat(enteredSuccessBranch[0], is(equalTo(true)));
    }

    @Test
    public void mapOfFailureShouldRenderFailure() throws InterruptedException {
        boolean[] enteredFailureBranch = { false };
        ResultOperation<String> resultOperation01 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            return Result.failure(new IllegalArgumentException("Failure by design"));
        });

        resultOperation01
                .map(str -> str.toUpperCase())
                .subscribe(
                        str -> {
                            // do nothing, expect to enter in failure branch
                        },
                        err -> {
                            enteredFailureBranch[0] = true;
                            return null;
                        });

        TimeUnit.MILLISECONDS.sleep(1000);
        Assert.assertThat(enteredFailureBranch[0], is(equalTo(true)));
    }

    @Test
    public void flatMapOfSuccessShouldRenderSuccess() throws InterruptedException {
        boolean[] enteredSuccessBranch = { false };
        ResultOperation<String> resultOperation01 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            return Result.success(COMPARE_STRING);
        });

        resultOperation01.flatMap(str -> resultOperation01)
                .subscribe(str -> enteredSuccessBranch[0] = true);

        TimeUnit.MILLISECONDS.sleep(1000);

        Assert.assertThat(enteredSuccessBranch[0], is(equalTo(true)));
    }

    @Test
    public void flatMapOfFailureShouldRenderFailure() throws InterruptedException {
        boolean[] enteredFailureBranch = { false };
        ResultOperation<String> resultOperation01 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            return Result.failure(new IllegalArgumentException("Failure by design"));
        });
        ResultOperation<String> resultOperation02 = executorService -> executorService.submit((Callable<Result<String>>) () -> {
            return Result.success(COMPARE_STRING);
        });

        resultOperation01
                .flatMap(__ -> resultOperation02)
                .subscribe(
                        str -> {
                            // do nothing, expect to enter in failure branch
                        },
                        err -> {
                            enteredFailureBranch[0] = true;
                            return null;
                        });

        TimeUnit.MILLISECONDS.sleep(1000);
        Assert.assertThat(enteredFailureBranch[0], is(equalTo(true)));
    }
}
