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

import java.io.IOException;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.Key;
import de.gematik.ti.healthcardaccess.cardobjects.Password;

/**
 * Test {@link DeactivateCommand}
 *
 */
@RunWith(Theories.class)
public class DeactivateCommandTest {
    @DataPoint
    public static boolean[] boolAray = { true, false };
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link DeactivateCommand#DeactivateCommand()}
     */
    @Test
    public void shouldEqualDeactivateCommand() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.DEACTIVATECOMMAND_APDU, 1);
        final AbstractHealthCardCommand command = new DeactivateCommand();
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link DeactivateCommand#DeactivateCommand(Key, boolean)}
     */
    @Theory
    public void shouldEqualDeactivateCommandKeyDfSpecific(final boolean dfSpecific) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.DEACTIVATECOMMAND_APDU, 2, dfSpecific);
        final Key key = TestResource.KEY_PRK_EGK_AUT_CVC_E256;
        final AbstractHealthCardCommand command = new DeactivateCommand(key, dfSpecific);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link DeactivateCommand#DeactivateCommand(byte[])}
     */
    @Test
    public void shouldEqualDeactivateCommandReference() throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.DEACTIVATECOMMAND_APDU, 3);
        final byte[] reference = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_REFERENCE);
        final AbstractHealthCardCommand command = new DeactivateCommand(reference);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link DeactivateCommand#DeactivateCommand(Password, boolean)}
     */
    @Theory
    public void shouldEqualDeactivateCommandPasswordDfSpecific(final boolean dfSpecific) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.DEACTIVATECOMMAND_APDU, 4, dfSpecific);
        final Password password = TestResource.PASSWD;
        final AbstractHealthCardCommand command = new DeactivateCommand(password, dfSpecific);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
