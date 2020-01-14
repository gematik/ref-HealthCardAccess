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

/**
 * Do some simple checking
 */
public class ValueStateChecker implements ISanityChecker<Boolean> {

    private static final Logger LOG = LoggerFactory.getLogger(ValueStateChecker.class);
    private static ISanityChecker instance;
    private String errorMessage;

    public static ISanityChecker getInstance() {
        if (instance == null) {
            instance = new ValueStateChecker();
        }
        return instance;
    }

    @Override
    public void check(final Boolean rightState) throws SanityCheckFailedException {
        try {
            if (!rightState) {
                throw new SanityCheckFailedException(errorMessage);
            }
        } finally {
            clean();
        }
    }

    private void clean() {
        errorMessage = "";
    }

    @Override
    public ISanityChecker<Boolean> setMsgIncaseError(final String errMsgKey) {
        errorMessage = RESOURCE_BUNDLE.getString(errMsgKey);
        return instance;
    }
}
