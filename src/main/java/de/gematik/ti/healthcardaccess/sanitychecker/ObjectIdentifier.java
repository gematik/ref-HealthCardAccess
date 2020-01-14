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

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Die Klasse beschreibt das Attribut 'ObjectIdentifier' gemäß 7.1.1.5
 */
public class ObjectIdentifier {

    public static final String ANSIX9P256R1 = "ansix9p256r1";
    public static final String ANSIX9P384R1 = "ansix9p384r1";
    public static final String SECP256R1 = "secp256r1";
    public static final String SECP384R1 = "secp384r1";
    public static final String AUTHS_ISO9796_2_WITH_RSA_SHA256_MUTUAL = "authS_ISO9796-2Withrsa_sha256_mutual";
    public static final String BRAINPOOLP256R1 = "brainpoolP256r1";
    public static final String BRAINPOOLP384R1 = "brainpoolP384r1";
    public static final String BRAINPOOLP512R1 = "brainpoolP512r1";
    public static final String PACE_ECDH_GM_AES_CBC_CMAC_128 = "id-PACE-ECDH-GM-AES-CBC-CMAC-128";
    public static final String PACE_ECDH_GM_AES_CBC_CMAC_192 = "id-PACE-ECDH-GM-AES-CBC-CMAC-192";
    public static final String PACE_ECDH_GM_AES_CBC_CMAC_256 = "id-PACE-ECDH-GM-AES-CBC-CMAC-256";
    public static final String PACE_ECDH_GM_AES_CBC_CMAC_128_PCD = "id-PACE-PCD-ECDH-GM-AES-CBC-CMAC-128";
    public static final String PACE_ECDH_GM_AES_CBC_CMAC_192_PCD = "id-PACE-PCD-ECDH-GM-AES-CBC-CMAC-192";
    public static final String PACE_ECDH_GM_AES_CBC_CMAC_256_PCD = "id-PACE-PCD-ECDH-GM-AES-CBC-CMAC-256";
    public static final String RSAES_OAEP = "id-RSAES-OAEP";
    public static final String RSA_ENCRYPTION = "rsaEncryption";
    public static final String SIGS_ISO9796_2_WITH_RSA_SHA256 = "sigS_ISO9796-2Withrsa_sha256";
    public static final String ECDSA_WITH_SHA256 = "ecdsa-with-SHA256";
    public static final String ECDSA_WITH_SHA384 = "ecdsa-with-SHA384";
    public static final String ECDSA_WITH_SHA512 = "ecdsa-with-SHA512";
    public static final String ELC_SHARED_SECRET_CALCULATION = "elcSharedSecretCalculation";
    public static final String ID_ELC_SHARED_SECRET_CALCULATION = "id-ELC-shared-secret-calculation";
    public static final String AUTS_GEMSPEC_COS_G2_ECC_WITH_SHA256 = "authS_gemSpec-COS-G2_ecc-with-sha256";
    public static final String AUTS_GEMSPEC_COS_G2_ECC_WITH_SHA384 = "authS_gemSpec-COS-G2_ecc-with-sha384";
    public static final String AUTS_GEMSPEC_COS_G2_ECC_WITH_SHA512 = "authS_gemSpec-COS-G2_ecc-with-sha512";
    public static final String CVC_FLAGLIST_TI = "cvc_FlagList_TI";
    public static final String CVC_FLAGLIST_CMS = "cvc_FlagList_CMS";

    private static ObjectIdentifier converterInstance;
    private String name;
    private final Hashtable<String, String> oidValueTable;

    /**
     * Der Konstruktor setzt die Basiswerte für die ObjectIdentifier.
     */
    public ObjectIdentifier() {
        this.oidValueTable = new Hashtable<>();
        this.oidValueTable.put(ANSIX9P256R1, "2A8648CE3D030107");
        this.oidValueTable.put(ANSIX9P384R1, "2B81040022");
        this.oidValueTable.put(SECP256R1, "2A8648CE3D030107");
        this.oidValueTable.put(SECP384R1, "2B81040022");
        this.oidValueTable.put(AUTHS_ISO9796_2_WITH_RSA_SHA256_MUTUAL, "2B2403050204");
        this.oidValueTable.put(BRAINPOOLP256R1, "2B2403030208010107");
        this.oidValueTable.put(BRAINPOOLP384R1, "2B240303020801010b");
        this.oidValueTable.put(BRAINPOOLP512R1, "2B240303020801010d");
        this.oidValueTable.put(PACE_ECDH_GM_AES_CBC_CMAC_128, "04007F00070202040202");
        this.oidValueTable.put(PACE_ECDH_GM_AES_CBC_CMAC_192, "04007F00070202040203");
        this.oidValueTable.put(PACE_ECDH_GM_AES_CBC_CMAC_256, "04007F00070202040204");
        this.oidValueTable.put(PACE_ECDH_GM_AES_CBC_CMAC_128_PCD, "04007F00070203040202");
        this.oidValueTable.put(PACE_ECDH_GM_AES_CBC_CMAC_192_PCD, "04007F00070203040203");
        this.oidValueTable.put(PACE_ECDH_GM_AES_CBC_CMAC_256_PCD, "04007F00070203040204");
        this.oidValueTable.put(RSAES_OAEP, "2A864886F70D010107");
        this.oidValueTable.put(RSA_ENCRYPTION, "2A864886F70D010101");
        this.oidValueTable.put(SIGS_ISO9796_2_WITH_RSA_SHA256, "2B240304020204");
        this.oidValueTable.put(ECDSA_WITH_SHA256, "2A8648CE3D040302");
        this.oidValueTable.put(ECDSA_WITH_SHA384, "2A8648CE3D040303");
        this.oidValueTable.put(ECDSA_WITH_SHA512, "2A8648CE3D040304");
        this.oidValueTable.put(AUTS_GEMSPEC_COS_G2_ECC_WITH_SHA256, "2B2403050301");
        this.oidValueTable.put(AUTS_GEMSPEC_COS_G2_ECC_WITH_SHA384, "2B2403050302");
        this.oidValueTable.put(AUTS_GEMSPEC_COS_G2_ECC_WITH_SHA512, "2B2403050303");
        this.oidValueTable.put(CVC_FLAGLIST_TI, "2A8214004C048118");
        this.oidValueTable.put(CVC_FLAGLIST_CMS, "2A8214004C048119");
    }

    /**
     * Der Konstruktor setzt die Basiswerte und setzt den Identifier auf den übergebenen Wert nach einer Überprüfung.
     * 
     * @param value
     *            - ObjectIdentifier
     */
    public ObjectIdentifier(final String value) throws Exception {
        this();
        if (!oidValueTable.contains(value)) {
            throw new Exception("Invalid OID");
        }
        name = ObjectIdentifier.convertToName(value);
    }

    public String getName() {
        return name;
    }

    /**
     * Konvertiert die Textrepräsentation eines ObjectIdentifiers in seinen Wert.
     * 
     * @param oid
     *            - Die Textrepräsentation eines ObjectIdentifiers
     * @return Der Wert des ObjectIdentifiers
     */
    public static String convertToValue(final String oid) {
        if (converterInstance == null) {
            converterInstance = new ObjectIdentifier();
        }
        return converterInstance.oidValueTable.get(oid);
    }

    /**
     * Konvertiert den Wert eines ObjectIdentifiers in seinen Namen.
     * 
     * @param value
     *            - Der Wert des ObjectIdentifiers
     * @return Der Name des ObjectIdentifiers
     */
    public static String convertToName(final String value) {
        if (converterInstance == null) {
            converterInstance = new ObjectIdentifier();
        }

        Enumeration<String> enumKeys = converterInstance.oidValueTable.keys();
        Enumeration<String> enumValues = converterInstance.oidValueTable.elements();

        while (enumValues.hasMoreElements()) {
            String refValue = enumValues.nextElement();
            if ((refValue.equalsIgnoreCase(value)) && (refValue.length() == value.length())) {
                return enumKeys.nextElement();
            }
            enumKeys.nextElement();
        }
        return "";
    }
}
