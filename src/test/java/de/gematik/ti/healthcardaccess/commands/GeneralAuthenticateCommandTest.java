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
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;

/**
 * Test {@link GeneralAuthenticateCommand}
 *
 */
@RunWith(Theories.class)
public class GeneralAuthenticateCommandTest {

    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * Test {@link GeneralAuthenticateCommand(boolean)}
     *
     * @param commandChaining commandChaining
     * @throws IOException if an error occurs
     */
    @Theory
    public void shouldEqualGeneralAuthenticateCommand(final boolean commandChaining) throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.GENERALAUTHENTICATECOMMAND_APDU, 1, commandChaining);
        AbstractHealthCardCommand command = new GeneralAuthenticateCommand(commandChaining);
        command.executeOn(card).test();
        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

    @Theory
    public void shouldEqualGeneralAuthenticateCommand1(final boolean commandChaining) throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.GENERALAUTHENTICATECOMMAND_APDU, 2, commandChaining);
        AbstractHealthCardCommand command = new GeneralAuthenticateCommand(commandChaining, new byte[] {}, 1);
        command.executeOn(card).test();
        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

    @Theory
    public void shouldEqualGeneralAuthenticateCommand3(final boolean commandChaining) throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.GENERALAUTHENTICATECOMMAND_APDU, 3, commandChaining);
        AbstractHealthCardCommand command = new GeneralAuthenticateCommand(commandChaining, new byte[] {}, 3);
        command.executeOn(card).test();
        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }
}
