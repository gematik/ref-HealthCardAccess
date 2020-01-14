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

import org.junit.Assert;

import de.gematik.ti.cardreader.provider.api.card.CardException;
import de.gematik.ti.cardreader.provider.api.card.ICard;
import de.gematik.ti.cardreader.provider.api.card.ICardChannel;
import de.gematik.ti.cardreader.provider.api.command.ICommandApdu;
import de.gematik.ti.cardreader.provider.api.command.IResponseApdu;
import de.gematik.ti.cardreader.provider.api.command.ResponseApdu;

public class TestChannel implements ICardChannel {

    private ICommandApdu lastCommandAPDU = null;
    private CrpMode crpMode;

    public TestChannel() {
    }

    public TestChannel(CrpMode crpMode) {
        this.crpMode = crpMode;
    }

    public byte[] getLastCommandAPDUBytes() {
        if (lastCommandAPDU != null) {
            return lastCommandAPDU.getBytes();
        } else {
            return new byte[0];
        }
    }

    @Override
    public ICard getCard() {
        return null;
    }

    @Override
    public int getChannelNumber() {
        return 0;
    }

    @Override
    public boolean isExtendedLengthSupported() {
        return true;
    }

    @Override
    public int getMaxMessageLength() {
        switch (crpMode) {
            case CARD_SIM:
                return 1033;
            case BLUETOOTH_FEITIAN:
                return 128;
            case IDENTOS:
                return 192;
            case TACTIVO:
                return 1033;
            case NFC:
                return 261;
            case PCSC:
                return 1033;
        }
        Assert.fail("Unknown CardReaderProvodier for test " + crpMode);
        return -1;
    }

    /**
     * Return value is set only for test purpose
     * @return
     */
    @Override
    public int getMaxResponseLength() {
        switch (crpMode) {
            case CARD_SIM:
                return 65536;
            case BLUETOOTH_FEITIAN:
                return 128;
            case IDENTOS:
                return 192;
            case TACTIVO:
                return 4096;
            case NFC:
                return 261;
            case PCSC:
                return 8192;
        }
        Assert.fail("Unknown CardReaderProvodier for test " + crpMode);
        return -1;
    }

    @Override
    public IResponseApdu transmit(final ICommandApdu commandAPDU) throws CardException {
        lastCommandAPDU = commandAPDU;
        return new ResponseApdu(new byte[] { (byte) 0x90, 0x00 });
    }

    @Override
    public void close() throws CardException {

    }

    /**
     * Simulate various CardReaderProvider
     */
    public enum CrpMode {
        CARD_SIM,
        BLUETOOTH_FEITIAN,
        IDENTOS,
        TACTIVO,
        PCSC,
        NFC
    }
}
