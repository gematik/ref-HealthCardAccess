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

public final class CheckPsoDecipherImpl {

    private CheckPsoDecipherImpl() {
    }

    private static final Response.ResponseStatus INCORRECT_PARAMETER_DATAFIELD = Response.ResponseStatus.WRONG_CIPHER_TEXT;
    private static final String TAG_MAC_DO = "8E";
    private static final String TAG_CIPHER_DO = "86";
    private static final String TAG_KEY_DO = "7F49";
    private static final String TAG_OID_DO = "06";
    private static final String TAG_CIPHER = "A6";
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

        // (N090.300) c. 1.
        if (!tlv.getTag().equalsIgnoreCase(TAG_CIPHER)) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

        ArrayList<BER_TLV> tlvList = tlv.getChildren();

        if ((4 != tlvList.size())
                // (N090.300) c. 2.
                || (!(tlvList.get(0).getTag().equalsIgnoreCase(TAG_OID_DO)))
                // (N090.300) c. 3.
                || (!(tlvList.get(1).getTag().equalsIgnoreCase(TAG_KEY_DO))) || (1 != (tlvList.get(1).getChildren().size()))
                || (!(tlvList.get(1).getChildren().get(0).getTag().equalsIgnoreCase(TAG_CIPHER_DO)))
                // (N090.300) c. 4.
                || (!(tlvList.get(2).getTag().equalsIgnoreCase(TAG_CIPHER_DO)))
                // (N090.300) c. 5.
                || (!(tlvList.get(3).getTag().equalsIgnoreCase(TAG_MAC_DO)))) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

        String oid = tlvList.get(0).getValue();
        String po = tlvList.get(1).getChildren().get(0).getValue();
        String c = tlvList.get(2).getValue();
        String t = tlvList.get(3).getValue();

        // (N090.300) c. 2.
        if (!(c.startsWith("02"))) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

    }
}
