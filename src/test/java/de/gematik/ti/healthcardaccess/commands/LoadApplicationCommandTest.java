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

/**
 * Test {@link LoadApplicationCommand}
 *
 */
@RunWith(Theories.class)
public class LoadApplicationCommandTest {
    @DataPoint
    public static boolean[] boolAray = { true, false };

    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link LoadApplicationCommand#LoadApplicationCommand(byte[], boolean)}
     *
     * @param commandChaining
     */
    @Theory
    public void shouldEqualLoadApplicationCommand(final boolean commandChaining) {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.LOADAPPLICATIONCOMMAND_APDU, commandChaining);
        final byte[] data = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_CMDDATA);
        final AbstractHealthCardCommand command = new LoadApplicationCommand(data, commandChaining);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
