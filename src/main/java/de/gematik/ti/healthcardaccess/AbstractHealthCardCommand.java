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

import static de.gematik.ti.healthcardaccess.operation.Result.evaluate;
import static de.gematik.ti.healthcardaccess.operation.ResultOperation.lazyUnitRo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gematik.ti.cardreader.provider.api.card.CardException;
import de.gematik.ti.cardreader.provider.api.card.ICardChannel;
import de.gematik.ti.cardreader.provider.api.command.CommandApdu;
import de.gematik.ti.cardreader.provider.api.command.ICommandApdu;
import de.gematik.ti.cardreader.provider.api.command.IResponseApdu;
import de.gematik.ti.healthcardaccess.operation.ResultOperation;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.utils.codec.Hex;

/**
 * Superclass for all HealthCardCommands
 *
 * @created 05-Jul-2018 14:33:51
 */
public abstract class AbstractHealthCardCommand implements IHealthCardCommand {
    protected static final int NE_MAX_EXTENDED_LENGTH = 65536;
    protected static final int NE_MAX_SHORT_LENGTH = 256;
    protected static final int EXPECT_ALL_WILDCARD = -1;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractHealthCardCommand.class);
    private static final int HEX_FF = 0xff;
    private static final int CODE_CHANNEL_CLOSED = 0x6881;

    protected final int cla;
    protected final int ins;
    protected int p1;
    protected int p2;
    protected byte[] data;
    protected Integer ne = null;

    protected Response response = null;

    protected AbstractHealthCardCommand(final int cla, final int ins) {
        this(cla, ins, (byte) 0x0, (byte) 0x0);
    }

    protected AbstractHealthCardCommand(final int cla, final int ins, final int p1, final int p2) { // NOCS (hoa): Smartcard command needs 4 parameters
        if (cla > HEX_FF || ins > HEX_FF || p1 > HEX_FF || p2 > HEX_FF) {
            throw new IllegalArgumentException("Parameter value exceeds one byte");
        }
        this.cla = cla;
        this.ins = ins;
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Returns the status response messages according to the calling command.
     *
     * @return
     */
    public abstract Map<Integer, Response.ResponseStatus> getStatusResponseMessages();

    private Response.ResponseStatus inferResponseStatusFromResponseCode(final int code) {
        if (code == CODE_CHANNEL_CLOSED) {
            return Response.ResponseStatus.CHANNEL_CLOSED;
        } else {
            return this.getStatusResponseMessages().getOrDefault(code, Response.ResponseStatus.UNKNOWN_EXCEPTION);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param iHealthCard
     *         health card to execute the command
     *
     * @return result operation
     *
     * @see IHealthCardCommand#executeOn(IHealthCard)
     */
    @Override
    public ResultOperation<Response> executeOn(final IHealthCard iHealthCard) {
        return lazyUnitRo(() -> evaluate(() -> this.sendApdu(iHealthCard.getCurrentCardChannel())));
    }

    protected Response sendApdu(final ICardChannel channel) throws CardException {
        ICommandApdu cApdu = getCommandApdu(channel);
        LOG.debug("APDU: " + cApdu.toString() + ", " + Hex.encodeHexString(cApdu.getBytes(), false));
        IResponseApdu responseAPDU = channel.transmit(cApdu);
        LOG.debug("response: " + responseAPDU.toString() + ", " + Hex.encodeHexString(responseAPDU.getBytes(), false));

        response = new Response(inferResponseStatusFromResponseCode(responseAPDU.getSW()), responseAPDU.getData());
        return response;
    }

    private ICommandApdu getCommandApdu(ICardChannel channel) throws CardException {
        ICommandApdu commandAPDU;
        Integer expectedLength = ne;
        if (expectedLength != null && expectedLength == EXPECT_ALL_WILDCARD) {
            if (channel.isExtendedLengthSupported()) {
                expectedLength = NE_MAX_EXTENDED_LENGTH;
            } else {
                expectedLength = NE_MAX_SHORT_LENGTH;
            }
        }

        if (data == null) {
            commandAPDU = new CommandApdu(cla, ins, p1, p2, expectedLength);
        } else {
            commandAPDU = new CommandApdu(cla, ins, p1, p2, data, expectedLength);
            int apduLength = commandAPDU.getBytes().length;
            if (apduLength > channel.getMaxMessageLength()) {
                throw new CardException("CommandApdu is too long to send. Limit for Reader is " + channel.getMaxMessageLength()
                        + " but length of commandApdu is " + apduLength);
            }
        }
        return commandAPDU;
    }

}
