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

import de.gematik.ti.cardreader.provider.api.card.ICardChannel;
import de.gematik.ti.healthcardaccess.IHealthCard;
import de.gematik.ti.healthcardaccess.IHealthCardStatus;
import de.gematik.ti.utils.codec.Hex;

public class TestHealthCard implements IHealthCard {
    private TestChannel channel = new TestChannel();
    private final TestChannel.CrpMode crpMode;

    public TestHealthCard() {
        crpMode = TestChannel.CrpMode.CARD_SIM;
    }

    public TestHealthCard(TestChannel.CrpMode crpMode) {
        this.crpMode = crpMode;
    }

    @Override
    public ICardChannel getCurrentCardChannel() {
        channel = new TestChannel(crpMode);
        return channel;
    }

    @Override
    public IHealthCardStatus getStatus() {
        return null;
    }

    public byte[] getLastCommandAPDUBytes() {
        return channel.getLastCommandAPDUBytes();
    }

    public String getLastCommandAPDUString() {
        return Hex.encodeHexString(channel.getLastCommandAPDUBytes());
    }

}
