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

public final class CheckPsoVerifyCertificateCommandImpl {

    private CheckPsoVerifyCertificateCommandImpl() {
    }

    private static final Response.ResponseStatus INCORRECT_PARAMETER_DATAFIELD = Response.ResponseStatus.WRONG_CIPHER_TEXT;
    private static final String TAG_CERTIFICATE_CONTENT_CASE_7F4E = "7F4E";
    private static final String TAG_SIGNATURE = "5F37";

    public static void check(final String errorMessage, final byte[] cmdData) {
        // (N095.000)
        // (N095.400)
        // (N095.900) a.
        // (N095.900) b.
        String strData = Hex.encodeHexString(cmdData);
        if (!BER_TLV.isValidTlv(strData)) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }
        BER_TLV tlv = new BER_TLV(strData);
        // (N095.900.b.2) certificate: 7F4E-L7F4E-certificateContent || 5F37-L5F37-signature
        if ((2 != tlv.getChildren().size()) || !tlv.getChildren().get(0).getTag().equals(TAG_CERTIFICATE_CONTENT_CASE_7F4E)
                || !tlv.getChildren().get(1).getTag().equals(TAG_SIGNATURE)) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

        // (N006.624)
        BER_TLV tlvCertificateContent = tlv.findTag(TAG_CERTIFICATE_CONTENT_CASE_7F4E);
        BER_TLV tlvSignature = tlv.findTag(TAG_SIGNATURE);

        // (N095.900) b. 2.
        if (tlvCertificateContent == null || tlvSignature == null) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

        if ((null == tlvSignature) || (null == tlvCertificateContent)) {
            throw new SanityCheckFailedException(errorMessage, INCORRECT_PARAMETER_DATAFIELD);
        }

    }

}
