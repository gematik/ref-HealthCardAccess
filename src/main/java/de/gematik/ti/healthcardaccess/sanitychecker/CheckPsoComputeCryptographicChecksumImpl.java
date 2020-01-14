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

import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.utils.codec.Hex;

public final class CheckPsoComputeCryptographicChecksumImpl {

    private CheckPsoComputeCryptographicChecksumImpl() {
    }

    private static final Response.ResponseStatus INCORRECT_PARAMETER_DATAFIELD = Response.ResponseStatus.WRONG_CIPHER_TEXT;
    private static final String TAG_MAC = "8E";
    private static final String TAG_DATA = "80";

    // TODO: CRYTO will be set correctlly in next impl. It needs some extension in the java class file
    private static final boolean CRYPTO = true;

    public static void check(final String errorMessage, final byte[] cmdData) {
        // TODO: CRYTO will be set correctlly in next impl. It needs some extension in the java class file
        if (CRYPTO) {
            return;
        }
        // (N095.000)
        // (N095.400)
        // (N095.900) a.
        // (N095.900) b.
        String strData = Hex.encodeHexString(cmdData);
        if (!BER_TLV.isValidTlv(strData)) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }
        BER_TLV tlv = new BER_TLV(strData);

        if (2 != tlv.getChildren().size()) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

        BER_TLV dataTlv = tlv.getChildren().get(0);
        BER_TLV macTlv = tlv.getChildren().get(1);

        if (!(dataTlv.getTag().equalsIgnoreCase(TAG_DATA)) || !(macTlv.getTag().equalsIgnoreCase(TAG_MAC))) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }
    }
}
