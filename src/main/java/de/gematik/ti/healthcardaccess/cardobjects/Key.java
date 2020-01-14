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

package de.gematik.ti.healthcardaccess.cardobjects;

import de.gematik.ti.healthcardaccess.ICardItem;

/**
 * Class applies for symmetric keys and private keys.
 *
 */
public class Key implements ICardItem, ICardKeyReference {
    private static final int MIN_KEY_ID = 2;
    private static final int MAX_KEY_ID = 28;

    private final int keyId;

    public Key(final int keyIdentifier) {
        this.keyId = keyIdentifier;
        sanityCheck();
    }

    public int getkeyId() {
        return keyId;
    }

    private void sanityCheck() {
        if (this.keyId < MIN_KEY_ID || this.keyId > MAX_KEY_ID) {
            // gemSpec_COS#N016.400 and #N017.100
            throw new IllegalArgumentException(String.format("Key ID out of range [%d,%d]", MIN_KEY_ID, MAX_KEY_ID));
        }
    }

    @Override
    public int calculateKeyReference(final boolean dfSpecific) {
        // gemSpec_COS#N099.600
        int keyReference = keyId;
        if (dfSpecific) {
            keyReference = keyReference + DF_SPECIFIC_PWD_MARKER;
        }
        return keyReference;
    }
}
