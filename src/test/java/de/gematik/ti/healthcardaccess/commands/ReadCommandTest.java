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
 * Test {@link ReadCommand}
 *
 */
public class ReadCommandTest {
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link ReadCommand#ReadCommand()}
     */
    @Test
    public void shouldEqualReadCommand() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.READCOMMAND_APDU, 1);
        final AbstractHealthCardCommand command = new ReadCommand();
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link ReadCommand#ReadCommand(int, int)}
     */
    @Test
    public void shouldEqualReadCommandOffsetNe() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.READCOMMAND_APDU, 2);
        final int offset = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_OFFSET);
        final int ne = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_NE);
        final AbstractHealthCardCommand command = new ReadCommand(offset, ne);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link ReadCommand#ReadCommand(int)}
     */
    @Test
    public void shouldEqualReadCommandOffset() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.READCOMMAND_APDU, 3);
        final int offset = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_OFFSET);
        final AbstractHealthCardCommand command = new ReadCommand(offset);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link ReadCommand#ReadCommand(ShortFileIdentifier)}
     */
    @Test
    public void shouldEqualReadCommandShortFileIdentifier() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.READCOMMAND_APDU, 4);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final AbstractHealthCardCommand command = new ReadCommand(sfi);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link ReadCommand#ReadCommand(ShortFileIdentifier, int)} }
     */
    @Test
    public void shouldEqualReadCommandShortFileIdentifierOffset() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.READCOMMAND_APDU, 5);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final int offset = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_OFFSET);
        final AbstractHealthCardCommand command = new ReadCommand(sfi, offset);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link ReadCommand#ReadCommand(ShortFileIdentifier, int, int)}
     */
    @Test
    public void shouldEqualReadCommandShortFileIdentifierOffsetNe() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.READCOMMAND_APDU, 6);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final int offset = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_OFFSET);
        final int ne = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_NE);
        final AbstractHealthCardCommand command = new ReadCommand(sfi, offset, ne);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

}
