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
import de.gematik.ti.healthcardaccess.cardobjects.Format2Pin;
import de.gematik.ti.healthcardaccess.cardobjects.Password;

/**
 * Test {@link EnableVerificationRequirementCommand}
 *
 */
@RunWith(Theories.class)
public class EnableVerificationRequirementCommandTest {

    @DataPoint
    public static boolean[] boolAray = { true, false };
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link EnableVerificationRequirementCommand#EnableVerificationRequirementCommand(Password, boolean, Format2Pin)}
     */
    @Theory
    public void shouldEqualEnableVerificationRequirementCommandPasswordDfSpecificFormat2Pin(final boolean dfSpecific) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.ENABLEVERIFICATIONREQUIREMENTCOMMAND_APDU, 1, dfSpecific);
        final Password password = TestResource.PASSWD;
        final Format2Pin verificationData = TestResource.PIN;
        final AbstractHealthCardCommand command = new EnableVerificationRequirementCommand(password, dfSpecific, verificationData);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link EnableVerificationRequirementCommand#EnableVerificationRequirementCommand(Password, boolean)}
     */
    @Theory
    public void shouldEqualEnableVerificationRequirementCommandPasswordDfSpecific(final boolean dfSpecific) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.ENABLEVERIFICATIONREQUIREMENTCOMMAND_APDU, 2, dfSpecific);
        final Password password = TestResource.PASSWD;
        final AbstractHealthCardCommand command = new EnableVerificationRequirementCommand(password, dfSpecific);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
