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

import java.util.Vector;

import de.gematik.ti.cardreader.provider.api.card.CardException;
import de.gematik.ti.cardreader.provider.api.card.ICard;
import de.gematik.ti.cardreader.provider.api.card.ICardChannel;
import de.gematik.ti.healthcardaccess.exceptions.runtime.BasicChannelException;
import de.gematik.ti.healthcardaccess.healthcards.HealthCardStatusInvalid;
import de.gematik.ti.healthcardaccess.healthcards.HealthCardStatusUnknown;
import de.gematik.ti.healthcardaccess.healthcards.HealthCardStatusValid;
import de.gematik.ti.healthcardaccess.healthcards.Unknown;

/**
 * HealthCard is the class that is used to bind the CardReaderProviderAPI channel to the HealthCardAccess layer
 *
 */
public class HealthCard implements IHealthCard {

    public static final int BASIC_CHANNEL_IDX = 0;
    protected static final int MAX_LOGICAL_CHANNELS = 4;

    private IHealthCardStatus status;

    private ICardChannel currentCardChannel;
    private final ICard associatedCard;

    private final Vector<ICardChannel> openChannels = new Vector<>(MAX_LOGICAL_CHANNELS + 1);

    /**
     * Instantiates a new HealthCard object with default IHealthCardStatus `HealthCardStatusUnknown`.
     *
     * @param card representation of physical card
     */
    public HealthCard(final ICard card) {
        this(card, new HealthCardStatusUnknown());
    }

    /**
     * Instantiates a new HealthCard object with a given IHealthCardStatus.
     *
     * @param card representation of physical card
     * @param status current status of health card
     */
    protected HealthCard(final ICard card, final IHealthCardStatus status) {
        this.associatedCard = card;
        this.status = status;
        try {
            currentCardChannel = card.openBasicChannel();
            openChannels.add(BASIC_CHANNEL_IDX, currentCardChannel);
        } catch (CardException e) {
            throw new BasicChannelException(e);
        }
    }

    /**
     * Open a new logical channel
     *
     * @return number of the new channel
     *
     * @throws WrongCardChannelException
     */
    public int openLogicalChannel() throws WrongCardChannelException {
        try {
            ICardChannel channel = associatedCard.openLogicalChannel();
            openChannels.add(channel.getChannelNumber(), channel);
            currentCardChannel = channel;
            return channel.getChannelNumber();
        } catch (CardException e) {
            throw new WrongCardChannelException(e);
        }
    }

    /**
     * Close the channel with the given id
     *
     * @param channelIdx
     *         id of the channel to close
     *
     * @throws WrongCardChannelException
     */
    public void closeChannel(final int channelIdx) throws WrongCardChannelException {
        try {
            ICardChannel channel = openChannels.get(channelIdx);
            if (channel != null) {
                channel.close();
                openChannels.remove(channelIdx);
            } else {
                throw new WrongCardChannelException("No such open channel, index = " + channelIdx);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new WrongCardChannelException(e);
        } catch (CardException ce) {
            throw new WrongCardChannelException(ce);
        }
    }

    /**
     * Set the current channel to use for the next operations
     *
     * @param channelIdx
     *         - id of the channel to use
     *
     * @throws WrongCardChannelException
     */
    public void setChannel(final int channelIdx) throws WrongCardChannelException {
        try {
            ICardChannel channel = openChannels.get(channelIdx);
            if (channel != null) {
                currentCardChannel = channel;
            } else {
                throw new WrongCardChannelException("No such open channel, index = " + channelIdx);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new WrongCardChannelException(e);
        }
    }

    /**
     * Return the current channel
     *
     * @return Current CardChannel object
     */
    @Override
    public ICardChannel getCurrentCardChannel() {
        return currentCardChannel;
    }

    /**
     * Get the current health card status
     *
     * @return status of the health card
     */
    @Override
    public IHealthCardStatus getStatus() {
        return status;
    }

    /**
     * Set the current health card status with the healthcard type
     * @param healthCardType
     */
    public void setHealthCardType(IHealthCardType healthCardType) {
        if (healthCardType instanceof Unknown) {
            status = new HealthCardStatusInvalid();
        } else {
            status = new HealthCardStatusValid(healthCardType);
        }
    }
}
