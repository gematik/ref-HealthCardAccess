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
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.SanityCheckFailedException;

/**
 * Test {@link FingerprintCommand}
 *
 */
public class FingerprintCommandTest {

    private static final Logger LOG = LoggerFactory.getLogger(FingerprintCommandTest.class);
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link FingerprintCommand#FingerprintCommand(byte[])}
     */
    @Test
    public void shouldEqualFingerprintCommand() {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.FINGERPRINTCOMMAND_APDU);
        final byte[] prefix = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_FINGERPRINT);
        final AbstractHealthCardCommand command = new FingerprintCommand(prefix);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Wrong prefix should cause throwing exception
     */
    @Test
    public void testSanityCheckNegative() {
        thrown.expect(SanityCheckFailedException.class);
        String errMsg = ISanityChecker.RESOURCE_BUNDLE.getString("FingerprintCommand.errMsg");
        thrown.expectMessage(errMsg);
        LOG.info(errMsg);
        final byte[] prefix = { 1, 2, 3 };
        new FingerprintCommand(prefix);

    }

}
