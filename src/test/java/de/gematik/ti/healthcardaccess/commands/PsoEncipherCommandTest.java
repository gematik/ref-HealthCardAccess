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

import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.PsoAlgorithm;
import de.gematik.ti.utils.codec.Hex;

/**
 * Test {@link PsoEncipher}
 *
 */
public class PsoEncipherCommandTest {
    private static final Logger LOG = LoggerFactory.getLogger(PsoEncipherCommandTest.class);
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();
    public static final byte[] TO_ENCIPHER_DATA = Hex.decode("ABCE441234");

    /**
     * {@link PsoEncipher#PsoEncipher(PsoAlgorithm, RSAPublicKey, byte[])} }
     */
    @Test
    public void shouldEqualPsoEncipherPsoAlgorithmRSAPublicKeyData() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.PSOENCIPHER_APDU, 1);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_ENCIPHER_PKCS1_V1_5);
        final RSAPublicKey publicKey = testResource.getRsaPublicKey();
        final AbstractHealthCardCommand command = new PsoEncipher(psoAlgorithm, publicKey, TO_ENCIPHER_DATA);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link PsoEncipher#PsoEncipher(PsoAlgorithm, ECPublicKey, byte[])} }
     */
    @Test
    public void shouldEqualPsoEncipherPsoAlgorithmECPublicKeyData() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.PSOENCIPHER_APDU, 2);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.DE_ENCRYPT_ELC_SHARED_SECRET_CALCULATION);
        final ECPublicKey publicKey = testResource.getEcPublicKey();
        final AbstractHealthCardCommand command = new PsoEncipher(psoAlgorithm, publicKey, TO_ENCIPHER_DATA);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link PsoEncipher#PsoEncipher(PsoAlgorithm, byte[])}
     */
    @Test
    public void shouldEqualPsoEncipherPsoAlgorithmData() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.PSOENCIPHER_APDU, 3);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_ENCIPHER_PKCS1_V1_5);
        final AbstractHealthCardCommand command = new PsoEncipher(psoAlgorithm, TO_ENCIPHER_DATA);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Test
    public void shouldEncryptOnCardWithTransmittedRsaKeyOaep() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.PSOENCIPHER_APDU, 4);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_ENCIPHER_OAEP);
        final RSAPublicKey rsaPublicKey = testResource.getRsaPublicKey();
        final AbstractHealthCardCommand command = new PsoEncipher(psoAlgorithm, rsaPublicKey, TO_ENCIPHER_DATA);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
