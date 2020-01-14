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
import de.gematik.ti.healthcardaccess.sanitychecker.SanityCheckFailedException;

/**
 * Test {@link GetRandomCommand}
 *
 */
public class GetRandomCommandTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetRandomCommandTest.class);
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link GetRandomCommand#GetRandomCommand(int)}
     */
    @Test
    public void shouldEqualGetRandomCommand() {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.GETRANDOMCOMMAND_APDU);
        final int numberOfExpectedOctetsInResponse = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_GETRANDOM);
        final AbstractHealthCardCommand command = new GetRandomCommand(numberOfExpectedOctetsInResponse);
        command.executeOn(card).test();

        Assert.assertThat(card.getLastCommandAPDUString(), IsEqual.equalTo(expectedAPDU));

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSanityCheckNegative() {
        thrown.expect(SanityCheckFailedException.class);
        String errMsg = "Argument numberOfExpectedOctetsInResponse 256 out of range [0,255].";
        thrown.expectMessage(errMsg);
        LOG.info(errMsg);
        final int numberOfExpectedOctets = 256;
        new GetRandomCommand(numberOfExpectedOctets);

    }
}
