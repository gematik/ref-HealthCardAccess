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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.PsoAlgorithm;

/**
 * Test {@link PsoDecipher}
 *
 */
public class PsoDecipherCommandTest {

    private static final Logger LOG = LoggerFactory.getLogger(PsoDecipherCommandTest.class);
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link PsoDecipher#PsoDecipher(PsoAlgorithm, byte[])}
     */
    @Test
    public void shouldEqualPsoDecipher() {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.PSODECIPHER_APDU);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_DECIPHER_PKCS1_V1_5);
        final byte[] dataToBeDeciphered = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new PsoDecipher(psoAlgorithm, dataToBeDeciphered);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }
}
