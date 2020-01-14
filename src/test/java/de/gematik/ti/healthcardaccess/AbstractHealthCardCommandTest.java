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

package de.gematik.ti.healthcardaccess;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.gematik.ti.cardreader.provider.api.card.CardException;
import de.gematik.ti.healthcardaccess.commands.FingerprintCommand;
import de.gematik.ti.healthcardaccess.commands.TestChannel;
import de.gematik.ti.healthcardaccess.commands.TestHealthCard;

/**
 * test indirectlly getCommandApdu(). (it is private i.e. not possible for a direct test)
 */
public class AbstractHealthCardCommandTest {

    private AbstractHealthCardCommand fingerPrintComamnd;
    private IHealthCard healthCard = null;

    @Before
    public void setUp() throws Exception {
        byte[] prefix = new byte[128];
        for (int i = 0; i < 128; i++) {
            prefix[i] = 1;
        }
        fingerPrintComamnd = new FingerprintCommand(prefix);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSendApduOnCardSimCrp() {
        try {
            healthCard = new TestHealthCard(TestChannel.CrpMode.CARD_SIM);
            TestChannel testChannel = (TestChannel) healthCard.getCurrentCardChannel();
            fingerPrintComamnd.sendApdu(testChannel);
            Assert.assertThat(testChannel.getLastCommandAPDUBytes().length, Is.is(137));
        } catch (CardException e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSendApduOnBluetoothCrp() {
        try {
            healthCard = new TestHealthCard(TestChannel.CrpMode.BLUETOOTH_FEITIAN);
            TestChannel testChannel = (TestChannel) healthCard.getCurrentCardChannel();
            fingerPrintComamnd.sendApdu(testChannel);
            Assert.fail("Expected error not thrown");
        } catch (CardException e) {
            Assert.assertThat(e.getMessage(), Is.is("CommandApdu is too long to send. Limit for Reader is 128 but length of commandApdu is 134"));
        }
    }

    @Test
    public void testSendApduOnNfcCrp() {
        try {
            healthCard = new TestHealthCard(TestChannel.CrpMode.NFC);
            TestChannel testChannel = (TestChannel) healthCard.getCurrentCardChannel();
            fingerPrintComamnd.sendApdu(testChannel);
            Assert.assertThat(testChannel.getLastCommandAPDUBytes().length, Is.is(137));
        } catch (CardException e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSendApduOnIdentosCrp() {
        try {
            healthCard = new TestHealthCard(TestChannel.CrpMode.IDENTOS);
            TestChannel testChannel = (TestChannel) healthCard.getCurrentCardChannel();
            fingerPrintComamnd.sendApdu(testChannel);
            Assert.assertThat(testChannel.getLastCommandAPDUBytes().length, Is.is(134));
        } catch (CardException e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSendApduOnTactivoCrp() {
        try {
            healthCard = new TestHealthCard(TestChannel.CrpMode.TACTIVO);
            TestChannel testChannel = (TestChannel) healthCard.getCurrentCardChannel();
            fingerPrintComamnd.sendApdu(testChannel);
            Assert.assertThat(testChannel.getLastCommandAPDUBytes().length, Is.is(137));
        } catch (CardException e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testSendApduOnPcscCrp() {
        try {
            healthCard = new TestHealthCard(TestChannel.CrpMode.PCSC);
            TestChannel testChannel = (TestChannel) healthCard.getCurrentCardChannel();
            fingerPrintComamnd.sendApdu(testChannel);
            Assert.assertThat(testChannel.getLastCommandAPDUBytes().length, Is.is(137));
        } catch (CardException e) {
            Assert.fail(e.toString());
        }
    }
}
