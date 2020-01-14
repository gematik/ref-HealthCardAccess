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
 * Test {@link UpdateCommand}
 *
 */
public class UpdateCommandTest {
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    @Test
    public void shouldEqualUpdateCommandData() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.UPDATECOMMAND_APDU, 1);

        final byte[] toWriteData = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new UpdateCommand(toWriteData);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

    @Test
    public void shouldEqualUpdateCommandOffsetData() {

        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.UPDATECOMMAND_APDU, 2);

        final int offset = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_OFFSET);
        final byte[] toWriteData = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new UpdateCommand(offset, toWriteData);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

    @Test
    public void shouldEqualUpdateCommandShortFileIdentifierData() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.UPDATECOMMAND_APDU, 3);

        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final byte[] toWriteData = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new UpdateCommand(sfi, toWriteData);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

    @Test
    public void shouldEqualUpdateCommandShortFileIdentifierOffsetData() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.UPDATECOMMAND_APDU, 4);

        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final int offset = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_OFFSET);
        final byte[] toWriteData = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new UpdateCommand(sfi, offset, toWriteData);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }
}
