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

import de.gematik.ti.utils.codec.Hex;

/**
 * It is possible that the attribute type shortFileIdentifier is used by the file object types.
 * Short file identifiers are used  for implicit file selection in the immediate context of a command.
 * The value of shortFileIdentifier MUST be an integer in the interval [1, 30]
 *
 * @see "ISO/IEC7816-4 und gemSpec_COS 'Spezifikation des Card Operating System'"
 *
 */
public class ShortFileIdentifier implements ICardObjectIdentifier {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 30;
    private final int sfId;

    public ShortFileIdentifier(final int sfId) {
        this.sfId = sfId;
        sanityCheck();
    }

    public ShortFileIdentifier(final String hexSfId) {
        this(Hex.decode(hexSfId)[0]);
    }

    public int getSfId() {
        return sfId;
    }

    private void sanityCheck() {
        if (this.sfId < MIN_VALUE || this.sfId > MAX_VALUE) {
            // gemSpec_COS#N007.000
            throw new IllegalArgumentException(String.format("Short File Identifier out of valid range [%d,%d]", MIN_VALUE, MAX_VALUE));
        }
    }
}
