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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.utils.codec.Hex;

public final class CheckPsoVerifyDigitalSignatureImpl {

    private CheckPsoVerifyDigitalSignatureImpl() {
    }

    private static final String TAG_POB = "86";
    private static final String TAG_PUBKEY_7F49 = "7F49";
    private static final String TAG_SIGNATURE = "9E";
    private static final String TAG_PUBKEY_9C = "9C";
    private static final String TAG_HASH = "90";
    private static final String TAG_OID = "06";
    private static final Response.ResponseStatus INCORRECT_PARAMETER_DATAFIELD = Response.ResponseStatus.WRONG_CIPHER_TEXT;
    private static final Logger LOG = LoggerFactory.getLogger(CheckPsoVerifyDigitalSignatureImpl.class);

    public static void check(final String errorMessage, final byte[] cmdData) {
        String strData = Hex.encodeHexString(cmdData);

        if (!BER_TLV.isValidTlv(strData)) {
            LOG.info("strData: {}", strData);
            throw new SanityCheckFailedException(errorMessage + " check (isValidTlv)", INCORRECT_PARAMETER_DATAFIELD);
        }
        BER_TLV tlv = new BER_TLV(strData);

        // (N096.386)
        // (N096.394)
        if ((tlv.getChildren().size() != 4) || !(tlv.getChildren().get(0).getTag().equalsIgnoreCase(TAG_OID))
                || !(tlv.getChildren().get(1).getTag().equalsIgnoreCase(TAG_HASH)) || !(tlv.getChildren().get(2).getTag().equalsIgnoreCase(TAG_PUBKEY_9C))
                || !(tlv.getChildren().get(3).getTag().equalsIgnoreCase(TAG_SIGNATURE))) {
            throw new SanityCheckFailedException(errorMessage + " check (N096.386)", INCORRECT_PARAMETER_DATAFIELD);
        }
        // (N096.394) a.
        String oid = ObjectIdentifier.convertToName(tlv.getChildren().get(0).getValue());
        String hash = tlv.getChildren().get(1).getValue();
        BER_TLV tlvPublicKey = new BER_TLV(tlv.getChildren().get(2).getValue());
        String signature = tlv.getChildren().get(3).getValue();

        // (N096.394) b.
        if ((tlvPublicKey.getChildren().size() != 1) || !(tlvPublicKey.getTag().equalsIgnoreCase(TAG_PUBKEY_7F49))) {
            throw new SanityCheckFailedException(errorMessage + " check (N096.394) a.", INCORRECT_PARAMETER_DATAFIELD);
        }

        BER_TLV tlvPOb = tlvPublicKey.getChildren().get(0);
        if (!(tlvPOb.getTag().equalsIgnoreCase(TAG_POB))) {
            throw new SanityCheckFailedException(errorMessage + " check (TAG_POB)", INCORRECT_PARAMETER_DATAFIELD);
        }

        // (N096.381)
        // String pOb = tlvPOb.getValue();

        int expectedHashLength = 0;
        int expectedSignatureLength = 0;

        // (N096.380)
        // (N096.394) d.
        if (oid.equalsIgnoreCase(ObjectIdentifier.ANSIX9P256R1) || oid.equalsIgnoreCase(ObjectIdentifier.SECP256R1)) {
            // (N096.382) a.
            expectedHashLength = 32;
            // (N096.383) a.
            expectedSignatureLength = 64;
        } else if (oid.equalsIgnoreCase(ObjectIdentifier.ANSIX9P384R1) || oid.equalsIgnoreCase(ObjectIdentifier.SECP384R1)) {
            // (N096.382) b.
            expectedHashLength = 48;
            // (N096.383) b.
            expectedSignatureLength = 96;
        } else if (oid.equalsIgnoreCase(ObjectIdentifier.BRAINPOOLP256R1)) {
            // (N096.382) c.
            expectedHashLength = 32;
            // (N096.383) c.
            expectedSignatureLength = 64;
        } else if (oid.equalsIgnoreCase(ObjectIdentifier.BRAINPOOLP384R1)) {
            // (N096.382) d.
            expectedHashLength = 48;
            // (N096.383) d.
            expectedSignatureLength = 96;
        } else if (oid.equalsIgnoreCase(ObjectIdentifier.BRAINPOOLP512R1)) {
            // (N096.382) e.
            expectedHashLength = 64;
            // (N096.383) e.
            expectedSignatureLength = 128;
        } else {
            throw new SanityCheckFailedException(errorMessage + " check expectedSignatureLength " + expectedSignatureLength, INCORRECT_PARAMETER_DATAFIELD);
        }

        // (N096.382)
        // (N096.394) a.
        if (expectedHashLength * 2 != hash.length()) {
            throw new SanityCheckFailedException(errorMessage + " check ()", INCORRECT_PARAMETER_DATAFIELD);
        }

        // (N096.383)
        // (N096.394) c.
        if (expectedSignatureLength * 2 != signature.length()) {
            throw new SanityCheckFailedException(errorMessage + " check (N096.383)", INCORRECT_PARAMETER_DATAFIELD);
        }

        // (N096.394) c.
        // String r = signature.substring(0, signature.length() / 2);
        // String s = signature.substring(signature.length() / 2);

    }
}
