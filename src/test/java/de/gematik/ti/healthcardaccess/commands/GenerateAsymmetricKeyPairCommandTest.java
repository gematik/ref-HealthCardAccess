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
import de.gematik.ti.healthcardaccess.cardobjects.Key;
import de.gematik.ti.healthcardaccess.commands.GenerateAsymmetricKeyPairCommand.GakpUseCase;

/**
 * Test {@link GenerateAsymmetricKeyPairCommand}
 *
 */
@RunWith(Theories.class)
public class GenerateAsymmetricKeyPairCommandTest {

    @DataPoint
    public static boolean[] boolAray = { true, false };
    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * {@link GenerateAsymmetricKeyPairCommand#GenerateAsymmetricKeyPairCommand(GakpUseCase, Key, boolean)}
     * 
     * @param dfSpecific
     */
    @Theory
    public void shouldEqualGenerateAsymmetricKeyPairCommand(final boolean dfSpecific) {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.GENERATEASYMMETRICKEYPAIRCOMMAND_APDU, dfSpecific);
        final GakpUseCase gakpUseCase = GakpUseCase.GEN_KEY_W_OVERWRITE_W_REFERENCE_W_OUTPUT;
        final Key key = TestResource.KEY_PRK_EGK_AUT_CVC_E256;

        final AbstractHealthCardCommand command = new GenerateAsymmetricKeyPairCommand(gakpUseCase, key, dfSpecific);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
