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

public final class CheckPsoEncipherImpl {

    private CheckPsoEncipherImpl() {
    }

    private static final Response.ResponseStatus INCORRECT_PARAMETER_DATAFIELD = Response.ResponseStatus.WRONG_CIPHER_TEXT;
    private static final String TAG_PUK_E = "82";
    private static final String TAG_PUK_N = "81";
    private static final String TAG_KEY_DO = "7F49";
    private static final String TAG_ALG_DO = "80";
    private static final String TAG_PLAIN_DO = "A0";
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
        ArrayList<BER_TLV> tlvList = tlv.getChildren();

        // (N090.788)
        // (N091.700) b. 2.
        // (N091.700) c. 2.
        // (N091.700) b. 3.
        // (N091.700) c. 3.
        // (N091.700) b. 4.
        // (N091.700) c. 4.
        if ((3 != tlvList.size()) || (!(tlvList.get(0).getTag().equalsIgnoreCase(TAG_ALG_DO))) || (!(tlvList.get(1).getTag().equalsIgnoreCase(TAG_KEY_DO)))
                || (2 != (tlvList.get(1).getChildren().size())) || (!(tlvList.get(1).getChildren().get(0).getTag().equalsIgnoreCase(TAG_PUK_N)))
                || (!(tlvList.get(1).getChildren().get(1).getTag().equalsIgnoreCase(TAG_PUK_E))) || (!(tlvList.get(2).getTag()
                        .equalsIgnoreCase(TAG_ALG_DO)))) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

    }
}
