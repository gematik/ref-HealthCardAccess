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
 * A password can be a regular password or multireference password
 *
 * * A "regular password" is used to store a secret, which is usually only known to one cardholder. The COS will allow certain services only if this secret has been successfully presented as part of a user verification. The need for user verification can be turned on (enable) or turned off (disable).
 * * A multireference password allows the use of a secret, which is stored as an at-tributary in a regular password (see (N015.200)), but under conditions that deviate from those of the regular password.
 *
 * @see "gemSpec_COS 'Spezifikation des Card Operating System'"
 *
 */
public class Password implements ICardItem, ICardKeyReference {
    private static final int MIN_PWD_ID = 0;
    private static final int MAX_PWD_ID = 31;

    private final int pwdId;

    public Password(final int pwdIdentifier) {
        this.pwdId = pwdIdentifier;
        sanityCheck();
    }

    public int getPwdId() {
        return pwdId;
    }

    private void sanityCheck() {
        if (this.pwdId < MIN_PWD_ID || this.pwdId > MAX_PWD_ID) {
            // gemSpec_COS#N015.000
            throw new IllegalArgumentException(String.format("Password ID out of range [%d,%d]", MIN_PWD_ID, MAX_PWD_ID));
        }
    }

    @Override
    public int calculateKeyReference(final boolean dfSpecific) {
        // gemSpec_COS#N072.800
        int passwordReference = pwdId;
        if (dfSpecific) {
            passwordReference = passwordReference + DF_SPECIFIC_PWD_MARKER;
        }
        return passwordReference;
    }
}
