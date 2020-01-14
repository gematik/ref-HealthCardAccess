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

import java.util.Arrays;

import de.gematik.ti.utils.codec.Hex;

/**
 * An application identifier (AID) is used to address an application on the card
 *
 */
public class ApplicationIdentifier implements ICardObjectIdentifier {

    private static final int AID_MIN_LENGTH = 5;
    private static final int AID_MAX_LENGTH = 16;

    private final byte[] aid;

    public ApplicationIdentifier(final String hexAid) {
        this.aid = Hex.decode(hexAid);
        sanityCheck();
    }

    public ApplicationIdentifier(final byte[] aid) {
        this.aid = Arrays.copyOf(aid, aid.length);
        sanityCheck();
    }

    public byte[] getAid() {
        return aid;
    }

    private void sanityCheck() {
        if (this.aid.length < AID_MIN_LENGTH || this.aid.length > AID_MAX_LENGTH) {
            // gemSpec_COS#N010.200
            throw new IllegalArgumentException(String.format("Application File Identifier length out of valid range [%d,%d]", AID_MIN_LENGTH, AID_MAX_LENGTH));
        }
    }
}
