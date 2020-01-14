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
import org.junit.Rule;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.SanityCheckFailedException;

/**
 * Test {@link ManageChannelCommand}
 *
 */
@RunWith(Theories.class)
public class ManageChannelCommandTest {
    private static final Logger LOG = LoggerFactory.getLogger(ManageChannelCommandTest.class);
    @DataPoint
    public static boolean[] boolAray = { true, false };

    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link ManageChannelCommand#ManageChannelCommand(boolean)}
     */
    @Theory
    public void shouldEqualManageChannelCommandBoolean(final boolean ifTrueCloseChannelElseResetChannel) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.MANAGECHANNELCOMMAND_APDU, 1, ifTrueCloseChannelElseResetChannel);
        final AbstractHealthCardCommand command = new ManageChannelCommand(ifTrueCloseChannelElseResetChannel);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link ManageChannelCommand#ManageChannelCommand(int, boolean)}
     */
    @Theory
    public void shouldEqualManageChannelCommandLogicalChannelNumberBoolean(final boolean ifTrueCloseChannelElseResetChannel) {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.MANAGECHANNELCOMMAND_APDU, 2, ifTrueCloseChannelElseResetChannel);
        final int logicalChannelNumber = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_CHANNELNUMBER);
        final AbstractHealthCardCommand command = new ManageChannelCommand(logicalChannelNumber, ifTrueCloseChannelElseResetChannel);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Theory
    public void testSanityCheckNegative(boolean ifTrueCloseChannelElseResetChannel) {
        thrown.expect(SanityCheckFailedException.class);
        String errMsg = ISanityChecker.RESOURCE_BUNDLE.getString("ManageChannelCommand.errMsg");
        thrown.expectMessage(errMsg);
        LOG.info(errMsg);
        final int logicalChannelNumber = 0;
        new ManageChannelCommand(logicalChannelNumber, ifTrueCloseChannelElseResetChannel);

    }
}
