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

/**
 * Represent a specific PSO Algorithm
 *
 * @see "ISO/IEC7816-4 und gemSpec_COS 'Spezifikation des Card Operating System'"
 *
 */
public class PsoAlgorithm {

    private final Algorithm algorithm;

    // algorithm specific fields
    private boolean flagSscMacIncrement = false;

    public PsoAlgorithm(final Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public int getIdentifier() {
        return this.algorithm.getIdentifier();
    }

    public String getName() {
        return this.algorithm.getName();
    }

    public boolean isFlagSscMacIncrement() {
        return flagSscMacIncrement;
    }

    public void setFlagSscMacIncrement(final boolean flag) {
        this.flagSscMacIncrement = flag;
    }

    public enum Algorithm {
        AUTHENTICATE_AES_SESSIONKEY_4SM(0x54, ""),
        AUTHENTICATE_DES_SESSIONKEY_4SM(0x54, ""),
        AUTHENTICATE_AES_SESSIONKEY_4TC(0x74, "aesSessionkey4TC"),
        AUTHENTICATE_DES_SESSIONKEY_4TC(0x74, "desSessionkey4TC (Option_DES)"),
        AUTHENTICATE_ELC_ASYNCHRON_ADMIN(0xF4, "elcAsynchronAdmin"),
        AUTHENTICATE_ELC_ROLE_AUTHENTICATION(0x00, "elcRoleAuthentication"),
        AUTHENTICATE_ELC_ROLE_CHECK(0x00, "elcRoleCheck"),
        AUTHENTICATE_ELC_SESSIONKEY_4SM(0x54, "elcSessionkey4SM"),
        AUTHENTICATE_ELC_SESSIONKEY_4TC(0xD4, "elcSessionkey4SM"),
        AUTHENTICATE_RSA_CLIENT_AUTHENTICATION(0x05, "rsaClientAuthentication"),
        AUTHENTICATE_RSA_ROLE_AUTHENTICATION_OPTION_CVC(0x00, "rsaRoleAuthentication"),
        AUTHENTICATE_RSA_ROLE_CHECK_OPTION_CVC(0x00, "rsaRoleCheck"),
        AUTHENTICATE_RSA_SESSIONKEY_4SM_OPTION_DES(0x54, "rsaSessionkey4SM"),
        AUTHENTICATE_RSA_SESSIONKEY_4TC_OPTION_DES(0x74, "rsaSessionkey4TC (Option_DES)"),
        DE_ENCRYPT_AES_SESSIONKEY(0x00, "aesSessionkey"),
        DE_ENCRYPT_DES_SESSIONKEY_OPTION_DES(0x00, "desSessionkey (Option_DES)"),
        DE_ENCRYPT_RSA_DECIPHER_OAEP(0x85, "rsaDecipherOaep"),
        DE_ENCRYPT_RSA_DECIPHER_PKCS1_V1_5(0x81, "rsaDecipherPKCS1_V1_5"),
        DE_ENCRYPT_RSA_ENCIPHER_OAEP(0x05, "rsaEncipherOaep"),
        DE_ENCRYPT_RSA_ENCIPHER_PKCS1_V1_5(0x01, "rsaEncipherPKCS1_V1_5"),
        DE_ENCRYPT_ELC_SHARED_SECRET_CALCULATION(0x0B, "elcSharedSecretCalculation"),
        SIGN_VERIFY_AES_SESSIONKEY(0x00, ""),
        SIGN_VERIFY_DES_SESSIONKEY_OPTION_DES(0x00, ""),
        SIGN_VERIFY_SIGN9796_2_DS2(0x07, ""),
        SIGN_VERIFY_SIGNPKCS1_V1_5(0x02, "signPKCS1_V1_5"),
        SIGN_VERIFY_PSS(0x05, "signPSS"),
        SIGN_VERIFY_ECDSA(0x00, "signECDSA");

        private final int identifier;
        private final String name;

        Algorithm(final int identifier, final String name) {
            this.identifier = identifier;
            this.name = name;
        }

        public int getIdentifier() {
            return this.identifier;
        }

        public String getName() {
            return name;
        }
    }

}
