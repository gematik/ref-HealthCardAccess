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

package de.gematik.ti.healthcardaccess.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import de.gematik.ti.healthcardaccess.cardobjects.ApplicationIdentifier;
import de.gematik.ti.healthcardaccess.cardobjects.FileIdentifier;
import de.gematik.ti.healthcardaccess.cardobjects.Format2Pin;
import de.gematik.ti.healthcardaccess.cardobjects.GemCvCertificate;
import de.gematik.ti.healthcardaccess.cardobjects.Key;
import de.gematik.ti.healthcardaccess.cardobjects.Password;
import de.gematik.ti.healthcardaccess.cardobjects.ShortFileIdentifier;

public class TestResource {

    private static final Logger LOG = LoggerFactory.getLogger(TestResource.class);
    private static final int ID_PIN_CH = 01;
    private static final int ID_PRK_EGK_AUT_CVC_E256 = 9;
    public static final Key KEY_PRK_EGK_AUT_CVC_E256 = new Key(ID_PRK_EGK_AUT_CVC_E256);

    public static final Password PASSWD = new Password(ID_PIN_CH);
    public static final Format2Pin PIN = new Format2Pin(new int[] { 1, 2, 3, 4, 5, 6 });

    /**
     * Convert a Hex String in a Byte Array. Spaces will be removed.
     *
     * @param hexString
     * @return
     */
    public static byte[] convertByteArray(final String hexString) {
        if (hexString == null) {
            return null;
        }
        String hex = formatHexString(hexString, false);

        final byte[] result = new byte[hex.length() / 2];
        final char[] enc = hex.toCharArray();
        for (int i = 0; i < enc.length; i += 2) {
            StringBuilder curr = new StringBuilder(2);
            curr.append(enc[i]).append(enc[i + 1]);
            result[i / 2] = (byte) Integer.parseInt(curr.toString(), 16);
        }

        return result;
    }

    /**
     * Format a hexadecimal String. <br>
     * String 'd3f246' will be formatted to 'D3 F2 46'<br>
     * String '13aB5' will be formatted to '01 3A B5'
     *
     * @param hexString
     * @param insertSpaces
     * @return String
     */
    public static String formatHexString(final String hexString, final boolean insertSpaces) {
        if (hexString == null) {
            return null;
        }

        String hex = hexString.replace(" ", "");
        hex = hex.toUpperCase();

        // Correct a odd length if any
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        if (insertSpaces) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hex.length(); i++) {
                sb.append(hex.charAt(i));
                if (i % 2 == 1 && i != hex.length() - 1) {
                    sb.append(" ");
                }
            }
            hex = sb.toString();
        }

        return hex;
    }

    private static ECPublicKey loadECPublicKey(final byte[] data) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", new org.spongycastle.jce.provider.BouncyCastleProvider());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(data);
            return (ECPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            LOG.error("EC. data: " + data, e);
        }
        return null;
    }

    private static RSAPublicKey loadRsaPublicKey(final byte[] data) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", new org.spongycastle.jce.provider.BouncyCastleProvider());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(data);
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            LOG.error("rsa. data: " + data, e);
        }
        return null;
    }

    private LinkedHashMap<String, Map<String, String>> expectedApdusYml;

    private LinkedHashMap<String, String> testParameterYml;

    @SuppressWarnings("unchecked")
    public TestResource() {
        try {
            Yaml yaml = new Yaml();
            String pathname = ClassLoader.getSystemResource("expectApdu.yml").getPath();
            Object yamlContent = yaml.load(new FileInputStream(new File(pathname)));
            expectedApdusYml = (LinkedHashMap<String, Map<String, String>>) yamlContent;
        } catch (FileNotFoundException e) {
            LOG.error("file: expectApdu.yml", e);
        }
        try {
            Yaml yaml = new Yaml();
            String pathname = ClassLoader.getSystemResource("testParameters.yml").getPath();
            Object yamlContent = yaml.load(new FileInputStream(new File(pathname)));
            testParameterYml = (LinkedHashMap<String, String>) yamlContent;
        } catch (FileNotFoundException e) {
            LOG.error("file: testParameters.yml", e);
        }
    }

    ECPublicKey getEcPublicKey() {
        String ecPublicKeyHex = testParameterYml.get(ParameterEnum.PARAMETER_ECPUBLICKEY.name());
        byte[] keyValueBytes = convertByteArray(ecPublicKeyHex);
        return loadECPublicKey(keyValueBytes);
    }

    /**
     * testId is required (count of test > 1)
     *
     * @param apduResultEnum
     * @param testId
     * @param booleanValue
     * @return
     */
    public String getExpectApdu(final ApduResultEnum apduResultEnum, final int testId, final boolean... booleanValue) {
        Map<String, String> map = expectedApdusYml.get(apduResultEnum.name() + "-" + testId);
        String booleanStrValues = "";
        for (boolean bool : booleanValue) {
            booleanStrValues = booleanStrValues + "-" + bool;
        }
        String result = map.get("apdu" + booleanStrValues);
        return result;
    }

    /**
     * testId is not required (count of test = 1)
     *
     * @param apduResultEnum
     * @return
     */
    public String getExpectApduWithoutTestID(final ApduResultEnum apduResultEnum, final boolean... booleanValue) {
        Map<String, String> map = expectedApdusYml.get(apduResultEnum.name());
        String booleanStrValues = "";
        for (boolean bool : booleanValue) {
            booleanStrValues = booleanStrValues + "-" + bool;
        }
        String result = map.get("apdu" + booleanStrValues);
        return result;
    }

    public GemCvCertificate getGemCvcCertificate() {
        String cvcString = testParameterYml.get(ParameterEnum.PARAMETER_GEMCVC.name());

        byte[] bytes = convertByteArray(cvcString);
        try {
            GemCvCertificate cvc = new GemCvCertificate(bytes);
            return cvc;
        } catch (IOException e) {
            LOG.error(e.toString(), e);
        }
        return null;
    }

    public Object getParameter(final ParameterEnum parameterEnum) {
        String ymlValue = testParameterYml.get(parameterEnum.name());
        if (parameterEnum.name().startsWith("PARAMETER_BYTEARRAY")) {
            return convertByteArray(ymlValue);
        }
        if (parameterEnum.name().startsWith("PARAMETER_INT")) {
            return new Integer(ymlValue);
        }
        switch (parameterEnum) {
            case PARAMETER_FILEIDENTIFIER:
                return new FileIdentifier(ymlValue);
            case PARAMETER_APPLICATIONIDENTIFIER:
                return new ApplicationIdentifier(ymlValue);
            case PARAMETER_FINGERPRINT:
                byte[] byteArray = new byte[128];
                for (int i = 0; i < 128; i++) {
                    byteArray[i] = (byte) i;
                }
                return byteArray;
            case PARAMETER_SID:
                return new ShortFileIdentifier(ymlValue);
        }
        return ymlValue;
    }

    public RSAPublicKey getRsaPublicKey() {
        String rsaKeyHex = testParameterYml.get(ParameterEnum.PARAMETER_RSAPUBLICKEY.name());
        RSAPublicKey rsaPublicKey = loadRsaPublicKey(convertByteArray(rsaKeyHex));
        return rsaPublicKey;
    }
}
