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

/**
 * Test {@link InternalAuthenticateCommand}
 *
 */
public class InternalAuthenticateCommandTest {
    private TestHealthCard card = new TestHealthCard();
    private TestResource testResource = new TestResource();

    /**
     * {@link InternalAuthenticateCommand#InternalAuthenticateCommand(PsoAlgorithm, byte[])}
     */
    @Test
    public void shouldEqualInternalAuthenticateCommand() {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.INTERNALAUTHENTICATECOMMAND_APDU);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.AUTHENTICATE_ELC_ASYNCHRON_ADMIN);
        final byte[] token = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_INTERNLAUTH);
        final AbstractHealthCardCommand command = new InternalAuthenticateCommand(psoAlgorithm, token);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
