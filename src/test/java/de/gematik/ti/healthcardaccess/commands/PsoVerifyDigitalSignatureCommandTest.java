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

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.sanitychecker.SanityCheckFailedException;
import de.gematik.ti.utils.codec.Hex;

/**
 * Test {@link PsoVerifyDigitalSignatureCommand}
 *
 */
public class PsoVerifyDigitalSignatureCommandTest {

    private static final Logger LOG = LoggerFactory.getLogger(PsoVerifyDigitalSignatureCommandTest.class);
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link PsoVerifyDigitalSignatureCommand#PsoVerifyDigitalSignatureCommand(ECPublicKey, byte[], byte[])}
     */
    @Test
    public void shouldEqualPsoVerifyDigitalSignatureCommand() {
        // TODO: use valid cmdData
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.PSOVERIFYDIGITALSIGNATURECOMMAND_APDU);
        final ECPublicKey ecPublicKey = testResource.getEcPublicKey();
        final byte[] hash = Hex.decode(
                "6691A8D098B317D8AAE2256632F294A190F8C775334FC2B5001DE79856A11EF8");
        final byte[] signature = Hex.decode(
                "20B7D1F2D57E8A8DE38B50D651D3C9B60613831A622DBC4F171461014B60CEB42A317697EA02AFA66EBD61F4DB0BEF22ADC84A1302C49D683064995612328309");
        final AbstractHealthCardCommand command = new PsoVerifyDigitalSignatureCommand(ecPublicKey, hash, signature);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSanityCheckNegative() {
        thrown.expect(SanityCheckFailedException.class);
        String errMsg = "Data in Command must be in valid structure. Error is WRONG_CIPHER_TEXT.";
        thrown.expectMessage(errMsg);
        LOG.info(errMsg);
        final ECPublicKey ecPublicKey = testResource.getEcPublicKey();
        final byte[] hash = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final byte[] signature = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        new PsoVerifyDigitalSignatureCommand(ecPublicKey, hash, signature);
    }
}
