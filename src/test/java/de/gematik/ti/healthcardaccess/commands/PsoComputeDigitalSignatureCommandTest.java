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

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.PsoAlgorithm;
import de.gematik.ti.utils.codec.Hex;

/**
 * Test {@link PsoComputeDigitalSignatureCommand}
 *
 */
public class PsoComputeDigitalSignatureCommandTest {

    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link PsoComputeDigitalSignatureCommand#PsoComputeDigitalSignatureCommand(PsoAlgorithm, byte[])}
     */
    @Test
    public void shouldEqualSignWithElc256OnHba21AndVerifySignatureLocally() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.PSOCOMPUTEDIGITALSIGNATURECOMMAND_APDU, 1);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.SIGN_VERIFY_ECDSA);
        final byte[] dataToBeSigned = Hex.decode("6691A8D098B317D8AAE2256632F294A190F8C775334FC2B5001DE79856A11EF8");
        // (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new PsoComputeDigitalSignatureCommand(psoAlgorithm, dataToBeSigned);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link PsoComputeDigitalSignatureCommand#PsoComputeDigitalSignatureCommand(PsoAlgorithm, byte[])}
     */
    @Test
    public void shouldEqualSignWithElc256OnHba21() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.PSOCOMPUTEDIGITALSIGNATURECOMMAND_APDU, 2);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.SIGN_VERIFY_ECDSA);
        final byte[] dataToBeSigned = Hex.decode("6691A8D098B317D8AAE2256632F294A190F8C775334FC2B5001DE79856A11EF8");
        // 0000206691A8D098B317D8AAE2256632F294A190F8C775334FC2B5001DE79856A11EF80000
        // byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new PsoComputeDigitalSignatureCommand(psoAlgorithm, dataToBeSigned);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link PsoComputeDigitalSignatureCommand#PsoComputeDigitalSignatureCommand(PsoAlgorithm, byte[])}
     */
    @Test
    public void shouldEqualWithRsaPkcsV115OnEgkGen2AndVerifySignatureLocally() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.PSOCOMPUTEDIGITALSIGNATURECOMMAND_APDU, 3);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.SIGN_VERIFY_SIGNPKCS1_V1_5);
        final byte[] dataToBeSigned = Hex.decode("3031300D0609608648016503040201050004206691A8D098B317D8AAE2256632F294A190F8C775334FC2B5001DE79856A11EF8");
        final AbstractHealthCardCommand command = new PsoComputeDigitalSignatureCommand(psoAlgorithm, dataToBeSigned);
        command.executeOn(card).test();
        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link PsoComputeDigitalSignatureCommand#PsoComputeDigitalSignatureCommand(PsoAlgorithm, byte[])}
     */
    @Test
    public void shouldEqualWithRsaPssOnHbaGen2() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.PSOCOMPUTEDIGITALSIGNATURECOMMAND_APDU, 4);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.SIGN_VERIFY_PSS);
        final byte[] dataToBeSigned = Hex
                .decode("3031300D0609608648016503040201050004206691A8D098B317D8AAE2256632F294A190F8C775334FC2B5001DE79856A11EF8"); // testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new PsoComputeDigitalSignatureCommand(psoAlgorithm, dataToBeSigned);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }
}
