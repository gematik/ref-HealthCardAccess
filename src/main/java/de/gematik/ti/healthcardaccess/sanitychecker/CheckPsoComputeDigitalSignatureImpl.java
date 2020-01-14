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

import java.util.ArrayList;

import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.utils.codec.Hex;

public final class CheckPsoComputeDigitalSignatureImpl {

    private CheckPsoComputeDigitalSignatureImpl() {
    }

    private static final Response.ResponseStatus INCORRECT_PARAMETER_DATAFIELD = Response.ResponseStatus.WRONG_CIPHER_TEXT;
    private static final String TAG_HASH = "90";
    private static final String TAG_PLAIN = "80";

    public static void check(final String errorMessage, final byte[] cmdData) {
        String strData = Hex.encodeHexString(cmdData);
        if (!BER_TLV.isValidTlv(strData)) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

        BER_TLV tlv = new BER_TLV(strData);

        // (N088.600) a. 1.
        ArrayList<BER_TLV> tlvList = tlv.getChildren();
        if (2 != tlvList.size()) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

        BER_TLV tlvPlain = tlvList.get(0);
        BER_TLV tlvHash = tlvList.get(1);

        // (N088.600) a. 2.
        // (N088.600) a. 3.
        if (!(tlvPlain.getTag().equalsIgnoreCase(TAG_PLAIN)) || !(tlvHash.getTag().equalsIgnoreCase(TAG_HASH))) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

        // (N087.700)
        if (tlvHash.getLengthInt() > 64) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

    }

}
