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

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.ApplicationIdentifier;
import de.gematik.ti.healthcardaccess.cardobjects.FileIdentifier;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

/**
 * Test {@link SelectCommand}
 *
 */
@RunWith(Theories.class)
public class SelectCommandTest {

    @DataPoint
    public static boolean[] boolAray = {true, false};
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(SelectCommandTest.class);
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link SelectCommand#SelectCommand(boolean, boolean)}
     */
    @Theory
    public void shouldEqualSelectCommandSelectParentElseRoot(final boolean selectParentElseRoot, final boolean readFirst) {
        System.out.println("selectParentElseRoot " + selectParentElseRoot + " " + readFirst);
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SELECTCOMMAND_APDU, 1, selectParentElseRoot, readFirst);
        final AbstractHealthCardCommand command = new SelectCommand(selectParentElseRoot, readFirst);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link SelectCommand#SelectCommand(ApplicationIdentifier)}
     */
    @Test
    public void shouldEqualSelectCommandApplicationIdentifier() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SELECTCOMMAND_APDU, 2);
        final ApplicationIdentifier aid = (ApplicationIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_APPLICATIONIDENTIFIER);
        final AbstractHealthCardCommand command = new SelectCommand(aid);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link SelectCommand#SelectCommand(ApplicationIdentifier, boolean, boolean, int)}
     */
    @Theory
    public void shouldEqualSelectCommandApplicationIdentifierSelectNextElseFirstOccurrenceRequestFcpFcpLength(final boolean selectNextElseFirstOccurrence,
                                                                                                              final boolean requestFcp) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SELECTCOMMAND_APDU, 3, selectNextElseFirstOccurrence, requestFcp);
        final ApplicationIdentifier aid = (ApplicationIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_APPLICATIONIDENTIFIER);
        final int fcpLength = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_FCPLENGTH);
        final AbstractHealthCardCommand command = new SelectCommand(aid, selectNextElseFirstOccurrence, requestFcp, fcpLength);
        command.executeOn(card).test();
        LOG.info("selectNextElseFirstOccurrence: {}; requestFcp: {}  apdu: {}", selectNextElseFirstOccurrence, requestFcp, card.getLastCommandAPDUString());
        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));
    }

    /**
     * {@link SelectCommand#SelectCommand(FileIdentifier, boolean)}
     */
    @Theory
    public void shouldEqualSelectCommandFileIdentifierSelectDfElseEf(final boolean selectDfElseEf) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SELECTCOMMAND_APDU, 4, selectDfElseEf);
        final FileIdentifier fid = (FileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_FILEIDENTIFIER);

        final AbstractHealthCardCommand command = new SelectCommand(fid, selectDfElseEf);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));

    }

    /**
     * {@link SelectCommand#SelectCommand(FileIdentifier, boolean, boolean, int)}
     */
    @Theory
    public void shouldEqualSelectCommandFileIdentifierSelectDfElseEfRequestFcpFcpLength(final boolean selectDfElseEf, final boolean requestFcp) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.SELECTCOMMAND_APDU, 5, selectDfElseEf, requestFcp);
        final FileIdentifier fid = (FileIdentifier) testResource.getParameter(ParameterEnum.PARAMETER_FILEIDENTIFIER);
        final int fcpLength = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_FCPLENGTH);
        final AbstractHealthCardCommand command = new SelectCommand(fid, selectDfElseEf, requestFcp, fcpLength);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));

    }

}
