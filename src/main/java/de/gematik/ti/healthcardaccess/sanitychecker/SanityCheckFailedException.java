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

import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gematik.ti.healthcardaccess.result.Response;

/**
 * Throw if {@link ISanityChecker#check(Object)} fails
 */
public class SanityCheckFailedException extends IllegalArgumentException {
    private static final Logger LOG = LoggerFactory.getLogger(SanityCheckFailedException.class);

    /**
     * Case: no placeholder in errorMessage
     * @param errorMessage
     */
    public SanityCheckFailedException(final String errorMessage) {
        super(errorMessage);
    }

    public SanityCheckFailedException(final String errorMessage, final int value, final int minValue, final int maxValue) {
        super(checkAndFormat(errorMessage, value, minValue, maxValue));
    }

    public SanityCheckFailedException(final String errorMessage, final List<Object> listValidPsoAlg, final String psoAlgName) {
        super(checkAndFormat(errorMessage, listValidPsoAlg, psoAlgName));
    }

    public SanityCheckFailedException(final String errorMessage, final int lengthCmdData, final int lengthSpec) {
        super(checkAndFormat(errorMessage, lengthCmdData, lengthSpec));
    }

    public SanityCheckFailedException(final String errorMessage, final Response.ResponseStatus ec) {
        super(checkAndFormat(errorMessage, ec));
    }

    private static String checkAndFormat(final String errorMessage, final Object value, final Object minValue, final Object maxValue) {
        int countPlaceholder = new StringTokenizer(errorMessage, "%").countTokens() - 1;
        if (countPlaceholder != 3) {
            LOG.warn("errorMessage '{}' not well defined. 3 Placeholder wished, but it is '{}'.", errorMessage, countPlaceholder);
            LOG.warn("errorMessage=" + errorMessage + "value=" + value + "minValue=" + minValue + "maxValue=" + maxValue);
        }
        return String.format(errorMessage, value, minValue, maxValue);

    }

    private static String checkAndFormat(final String errorMessage, final Object value, final Object name) {
        int countPlaceholder = new StringTokenizer(errorMessage, "%").countTokens() - 1;
        if (countPlaceholder != 2) {
            LOG.warn("errorMessage '{}' not well defined.  2 Placeholder wished, but it is '{}'.", errorMessage, countPlaceholder);
            LOG.warn("errorMessage=" + errorMessage + "value=" + value + "name=" + name);
        }
        return String.format(errorMessage, value, name);
    }

    private static String checkAndFormat(final String errorMessage, final Object ec) {
        int countPlaceholder = new StringTokenizer(errorMessage, "%").countTokens() - 1;
        if (countPlaceholder != 1) {
            LOG.warn("errorMessage '{}' not well defined.  1 Placeholder wished, but it is '{}'.", errorMessage, countPlaceholder);
            LOG.warn("errorMessage=" + errorMessage + "ErrCode=" + ec);
        }
        return String.format(errorMessage, ec);
    }

}
