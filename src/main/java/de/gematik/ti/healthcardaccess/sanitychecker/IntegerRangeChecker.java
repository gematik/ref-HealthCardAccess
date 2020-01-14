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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 */
public class IntegerRangeChecker implements ISanityChecker<Integer> {
    /**
     *
     */
    public static final int MIN_OFFSET_RANGE = 0;
    /**
     * gemSpec_COS#N011.500
     */
    public static final int MAX_OFFSET_WITHOUT_SFI_RANGE = 0x7FFF;
    /**
     * for example gemSpec_COS#14.3.4.2.b
     */
    public static final int MAX_OFFSET_WITH_SFI_RANGE = 255;
    /**
     * gemSpec_COS#N007.600
     */
    public static final int MIN_RECORD_NUMBER = 1;
    public static final int MAX_RECORD_NUMBER = 254;
    /**
     * gemSpec_COS#N007.700
     */
    public static final int MIN_RECORD_DATA_LENGTH = 1;
    public static final int MAX_RECORD_DATA_LENGTH = 255;
    /**
     * gemSpec_COS#N067.700
     */
    public static final int MIN_RECORD_SEARCH_STRING_LENGTH = 0;
    public static final int MAX_RECORD_SEARCH_STRING_LENGTH = 255;
    private static final Logger LOG = LoggerFactory.getLogger(IntegerRangeChecker.class);
    private static ISanityChecker instance;

    private Integer minValue;
    private Integer maxValue;
    private String errorMessage;

    public static ISanityChecker getInstance() {
        if (instance == null) {
            instance = new IntegerRangeChecker();
        }
        return instance;
    }

    @Override
    public void check(final Integer value) throws SanityCheckFailedException {
        try {
            Integer intValue = (Integer) value;
            checkInteger(intValue);
        } finally {
            clean();
        }
    }

    private void checkInteger(final Integer intValue) {
        checkByRange(intValue);
    }

    /**
     * minValue and maxValue must be configured already
     *
     * @param intValue
     */
    private void checkByRange(final int intValue) {
        if (intValue < minValue || intValue > maxValue) {
            throw new SanityCheckFailedException(errorMessage, intValue, minValue, maxValue);
        }

    }

    private void clean() {
        errorMessage = "";
        minValue = null;
        maxValue = null;
    }

    /**
     * Get the configured values for this checker
     *
     * @param key
     * @param value
     *
     * @return
     */
    @Override
    public ISanityChecker<Integer> setSpecialConfigurationPair(final String key, final int value) {
        try {
            Field field = getClass().getDeclaredField(key);
            field.set(instance, value);
        } catch (Exception e) {
            LOG.error("parameter is : {}, value: {}, err is {}", key, value, e.toString());
        }
        return instance;
    }

    @Override
    public ISanityChecker<Integer> setMsgIncaseError(final String errMsgKey) {
        errorMessage = RESOURCE_BUNDLE.getString(errMsgKey);
        return instance;
    }
}
