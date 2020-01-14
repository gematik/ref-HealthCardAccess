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
 * Test {@link WriteCommand}
 *
 */
public class WriteCommandTest {
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link WriteCommand#WriteCommand(byte[])}
     */
    @Test
    public void shouldEqualWriteCommandData() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.WRITECOMMAND_APDU, 1);
        final byte[] data = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new WriteCommand(data);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

    /**
     * {@link WriteCommand#WriteCommand(ShortFileIdentifier, byte[])}
     */
    @Test
    public void shouldEqualWriteCommandShortFileIdentifierData() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.WRITECOMMAND_APDU, 2);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final byte[] data = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new WriteCommand(sfi, data);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

}
