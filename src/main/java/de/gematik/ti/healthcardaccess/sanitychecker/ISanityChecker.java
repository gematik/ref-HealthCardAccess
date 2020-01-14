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

package de.gematik.ti.healthcardaccess.sanitychecker;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This interface defines how SanityCheck works. <br/>
 * Firstly: CONFIGURE checker with {@link #setMsgIncaseError(String)} and/or{@link #setSpecifiedValues(Object...)} and/or {@link #setSpecialConfigurationPair(Object, Object)} and/or {@link #setCurrentParameter(Object)}. <br/>
 * Then: RUN check(input)
 * FINALLY clean the singleton-instance (see the impl-classes)
 *
 * @param <T> the type of input to be checked. see {@link #check(Object)}
 */
public interface ISanityChecker<T> {
    static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("errorMessage", Locale.getDefault());

    /**
     * Check the <code>input</code>, if check failed, then throw an exception
     *
     * @param input
     * @throws SanityCheckFailedException
     */
    void check(T input) throws SanityCheckFailedException;

    /**
     * Set the 'right' value in gem-specification <br/>
     * This step must be done before running {@link #check(Object)} if it is used <br/>
     *
     * !Attention: values could be in differenct types by using 
     *
     * @param values
     * @return
     */
    default ISanityChecker<T> setSpecifiedValues(T... values) {
        return this;
    }

    /**
     * Set the value from gem-specification or special configuration as a pair of {key, value}<br/>
     * This step must be done before running {@link #check(Object)} if it is used
     *
     * @param key
     * @param value
     * @return
     */
    default ISanityChecker<T> setSpecialConfigurationPair(String key, int value) {
        return this;
    }

    /**
     * Set the parameter as an attribute which is used in {@link #check(Object)} <br/>
     * This step must be done before running {@link #check(Object)} if it is used
     *
     * @param parameter
     * @return
     */
    default <E> ISanityChecker<T> setCurrentParameter(E parameter) {
        return this;
    }

    /**
     * Get the whole error message from errorMessage.properties on errMsgKey
     * @param errMsgKey
     * @return
     */
    ISanityChecker<T> setMsgIncaseError(String errMsgKey);
}
