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

/**
 * Test {@link TerminateDfCommand}
 *
 */
public class TerminateDfCommandTest {

    private TestHealthCard card = new TestHealthCard();
    private TestResource testResource = new TestResource();

    @Test
    public void shouldEqualTerminateDfCommand() {
        final String expectedAPDU = testResource.getExpectApduWithoutTestID(ApduResultEnum.TERMINATEDFCOMMAND_APDU);
        final AbstractHealthCardCommand command = new TerminateDfCommand();
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

}
