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
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.PsoAlgorithm;

/**
 * Test {@link ExternalMutualAuthenticateCommand}
 *
 */
@RunWith(Theories.class)
public class ExternalMutualAuthenticateCommandTest {

    @DataPoint
    public static boolean[] boolAray = { true, false };
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link ExternalMutualAuthenticateCommand#ExternalMutualAuthenticateCommand(PsoAlgorithm, byte[], boolean)}
     *
     * @param expectResponseData
     */
    @Theory
    public void shouldEqualExternalMutualAuthenticateCommand(final boolean expectResponseData) {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.EXTERNALMUTUALAUTHENTICATECOMMAND_APDU, expectResponseData);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.AUTHENTICATE_ELC_ROLE_CHECK);
        final byte[] cmdData = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_EXTERNALAUTH);
        final AbstractHealthCardCommand command = new ExternalMutualAuthenticateCommand(psoAlgorithm, cmdData, expectResponseData);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Theory
    public void shouldEqualExternalMutualAuthenticateAesSMCommand(final boolean expectResponseData) {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.EXTERNALMUTUALAUTHENTICATECOMMAND_APDU, expectResponseData);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.AUTHENTICATE_AES_SESSIONKEY_4SM);
        final byte[] cmdData = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_EXTERNALAUTH);
        final AbstractHealthCardCommand command = new ExternalMutualAuthenticateCommand(psoAlgorithm, cmdData, expectResponseData);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
