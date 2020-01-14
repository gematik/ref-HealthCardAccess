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

package de.gematik.ti.healthcardaccess;

/**
 * Exception to report a card data exception
 *
 */
public class WrongCardDataException extends Exception {
    /**
     * Create a new Exception with the given cause exception
     * @param cause - cause of this exception
     */
    public WrongCardDataException(final Throwable cause) {
        super(cause);
    }

    /**
     * Create a new Exception with the given cause exception and specific message
     * @param message - specific message
     * @param cause - cause of this exception
     */
    public WrongCardDataException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new Exception with the given specific message
     * @param message - specific message
     */
    public WrongCardDataException(final String message) {
        super(message);
    }
}
