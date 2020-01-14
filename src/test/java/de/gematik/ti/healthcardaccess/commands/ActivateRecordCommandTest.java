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
import de.gematik.ti.healthcardaccess.cardobjects.ShortFileIdentifier;

/**
 * Test {@link ActivateRecordCommand}
 *
 */
@RunWith(Theories.class)
public class ActivateRecordCommandTest {

    @DataPoint
    public static boolean[] boolAray = { true, false };
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link ActivateRecordCommand#ActivateRecordCommand(int, boolean)}
     *
     * @param activateAllRecordsStartingFromRecordNumber
     */
    @Theory
    public void shouldEqualActivateRecordCommandRecordNumberActivateAllRecordsStartingFromRecordNumber(
            final boolean activateAllRecordsStartingFromRecordNumber) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.ACTIVATERECORDCOMMAND_APDU, 1, activateAllRecordsStartingFromRecordNumber);
        final int recordNumber = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_RECORDNUMBER);
        final AbstractHealthCardCommand command = new ActivateRecordCommand(recordNumber, activateAllRecordsStartingFromRecordNumber);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

    /**
     * {@link ActivateRecordCommand#ActivateRecordCommand(ShortFileIdentifier, int, boolean)}
     *
     * @param activateAllRecordsStartingFromRecordNumber
     */
    @Theory
    public void shouldEqualActivateRecordCommandShortFileIdentifierIntBoolean(final boolean activateAllRecordsStartingFromRecordNumber) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.ACTIVATERECORDCOMMAND_APDU, 2, activateAllRecordsStartingFromRecordNumber);
        final ShortFileIdentifier sfi = (ShortFileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_SID);
        final int recordNumber = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_RECORDNUMBER);
        final AbstractHealthCardCommand command = new ActivateRecordCommand(sfi, recordNumber, activateAllRecordsStartingFromRecordNumber);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));
    }

}
