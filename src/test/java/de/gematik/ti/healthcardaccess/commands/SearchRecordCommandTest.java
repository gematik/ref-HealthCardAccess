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
 * Test {@link SearchRecordCommand}
 *
 */
public class SearchRecordCommandTest {

    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link SearchRecordCommand#SearchRecordCommand(int, byte[])}
     */
    @Test
    public void shouldEqualSearchRecordCommandRecordNumberByteArray() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SEARCHRECORDCOMMAND_APDU, 1);
        final int recordNumber = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_RECORDNUMBER);
        final byte[] searchString = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new SearchRecordCommand(recordNumber, searchString);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link SearchRecordCommand#SearchRecordCommand(int, byte[], int)}
     */
    @Test
    public void shouldEqualSearchRecordCommandRecordNumberByteArrayNe() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SEARCHRECORDCOMMAND_APDU, 2);
        final int recordNumber = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_RECORDNUMBER);
        final byte[] searchString = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final int ne = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_NE);
        final AbstractHealthCardCommand command = new SearchRecordCommand(recordNumber, searchString, ne);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link SearchRecordCommand#SearchRecordCommand(int, byte[], int)}
     */
    @Test
    public void shouldEqualSearchRecordCommandShortFileIdentifierIntByteArrayNe() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SEARCHRECORDCOMMAND_APDU, 4);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final int recordNumber = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_RECORDNUMBER);
        final byte[] searchString = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final int ne = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_NE);
        final AbstractHealthCardCommand command = new SearchRecordCommand(sfi, recordNumber, searchString, ne);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link SearchRecordCommand#SearchRecordCommand(ShortFileIdentifier, int, byte[])}
     */
    @Test
    public void shouldEqualSearchRecordCommandShortFileIdentifierRecordNumberByteArray() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SEARCHRECORDCOMMAND_APDU, 3);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final int recordNumber = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_RECORDNUMBER);
        final byte[] searchString = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_DEFAULT);
        final AbstractHealthCardCommand command = new SearchRecordCommand(sfi, recordNumber, searchString);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }
}
