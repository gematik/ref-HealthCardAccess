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
import de.gematik.ti.healthcardaccess.cardobjects.ShortFileIdentifier;

/**
 * Test {@link SetLogicalEofCommand}
 *
 */
public class SetLogicalEofCommandTest {

    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    @Test
    public void shouldEqualSetLogicalEofCommand() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SETLOGICALEOFCOMMAND_APDU, 1);
        final AbstractHealthCardCommand command = new SetLogicalEofCommand();
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Test
    public void shouldEqualSetLogicalEofCommandOffset() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SETLOGICALEOFCOMMAND_APDU, 2);
        final int offset = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_OFFSET);
        final AbstractHealthCardCommand command = new SetLogicalEofCommand(offset);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Test
    public void shouldEqualSetLogicalEofCommandShortFileIdentifier() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SETLOGICALEOFCOMMAND_APDU, 3);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final AbstractHealthCardCommand command = new SetLogicalEofCommand(sfi);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Test
    public void shouldEqualSetLogicalEofCommandShortFileIdentifierOffset() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SETLOGICALEOFCOMMAND_APDU, 4);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final int offset = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_OFFSET);
        final AbstractHealthCardCommand command = new SetLogicalEofCommand(sfi, offset);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
