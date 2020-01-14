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

package de.gematik.ti.healthcardaccess.entities;

import de.gematik.ti.healthcardaccess.exceptions.runtime.Version2ReaderException;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.BERTags;
import org.spongycastle.asn1.DERApplicationSpecific;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Represent the Version2 information of HealthCard
 *
 */
public class Version2 {

    private byte[] data;
    private byte[] fillingInstructionsVersion = new byte[0]; // C0
    private byte[] objectSystemVersion = new byte[0]; // C1
    private byte[] productIdentificationObjectSystemVersion = new byte[0]; // C2

    private byte[] fillingInstructionsEfGdoVersion = new byte[0]; // C4
    private byte[] fillingInstructionsEfAtrVersion = new byte[0]; // C5
    private byte[] fillingInstructionsEfKeyInfoVersion = new byte[0]; // C6  //only  gSMC-K and gSMC-KT
    private byte[] fillingInstructionsEfEnvironmentSettingsVersion = new byte[0]; // C3  //only  gSMC-K
    private byte[] fillingInstructionsEfLoggingVersion = new byte[0]; // C7

    private Version2() {
    }

    /**
     * Create and fill a new instance of Version2 Object with available data from card response data
     *
     * @param data
     *         response data from card
     *
     * @return new instance of Version2
     *
     * @throws IOException
     */
    public static Version2 fromArray(byte[] data) {
        Version2 version2 = new Version2();
        version2.data = data;
        try {
            version2.readData();
        } catch (IOException e) {
            throw new Version2ReaderException(e);
        }
        return version2;
    }

    private void readData() throws IOException {
        try (ASN1InputStream decoder = new ASN1InputStream(data)) {
            DERApplicationSpecific app = (DERApplicationSpecific) decoder.readObject();
            if (app.isConstructed()) {
                handleConstructedContent(app);
            }
        }
    }

    private void handleConstructedContent(DERApplicationSpecific app) throws IOException {
        ASN1Sequence seq = (ASN1Sequence) app.getObject(BERTags.SEQUENCE);
        Enumeration secEnum = seq.getObjects();
        while (secEnum.hasMoreElements()) {
            ASN1Primitive seqObj = (ASN1Primitive) secEnum.nextElement();
            int applicationTag = DERApplicationSpecific.getInstance(seqObj).getApplicationTag();
            byte[] content = DERApplicationSpecific.getInstance(seqObj).getContents();
            handleTagData(applicationTag, content);
        }
    }

    private void handleTagData(int tag, byte[] content) {
        switch (tag) {
            case 0:
                fillingInstructionsVersion = content;
                break;
            case 1:
                objectSystemVersion = content;
                break;
            case 2:
                productIdentificationObjectSystemVersion = content;
                break;
            case 3:
                fillingInstructionsEfEnvironmentSettingsVersion = content;
                break;
            case 4:
                fillingInstructionsEfGdoVersion = content;
                break;
            case 5:
                fillingInstructionsEfAtrVersion = content;
                break;
            case 6:
                fillingInstructionsEfKeyInfoVersion = content;
                break;
            case 7:
                fillingInstructionsEfLoggingVersion = content;
                break;
            default:
                break;
        }
    }

    /**
     * Information of C0 with version of filling instruction for version2
     *
     * @return content of version data
     */
    public byte[] getFillingInstructionsVersion() {
        return fillingInstructionsVersion;
    }

    /**
     * Information of C1 with version of card object system
     *
     * @return content of version data
     */
    public byte[] getObjectSystemVersion() {
        return objectSystemVersion;
    }

    /**
     * Information of C2 with version of product identification object system
     *
     * @return content of version data
     */
    public byte[] getProductIdentificationObjectSystemVersion() {
        return productIdentificationObjectSystemVersion;
    }

    /**
     * Information of C4 with version of filling instruction for EF.GDO
     *
     * @return content of version data
     */
    public byte[] getFillingInstructionsEfGdoVersion() {
        return fillingInstructionsEfGdoVersion;
    }

    /**
     * Information of C5 with version of filling instruction for EF.ATR
     *
     * @return content of version data
     */
    public byte[] getFillingInstructionsEfAtrVersion() {
        return fillingInstructionsEfAtrVersion;
    }

    /**
     * Information of C6 with version of filling instruction for EF.KeyInfo
     * Only filled for gSMC-K and gSMC-KT
     *
     * @return content of version data
     */
    public byte[] getFillingInstructionsEfKeyInfoVersion() {
        return fillingInstructionsEfKeyInfoVersion;
    }

    /**
     * Information of C3 with version of filling instruction for Environment Settings
     * Only filled for gSMC-K
     *
     * @return content of version data
     */
    public byte[] getFillingInstructionsEfEnvironmentSettingsVersion() {
        return fillingInstructionsEfEnvironmentSettingsVersion;
    }

    /**
     * Information of C7 with version of filling instruction for EF.GDO
     *
     * @return content of version data
     */
    public byte[] getFillingInstructionsEfLoggingVersion() {
        return fillingInstructionsEfLoggingVersion;
    }
}
