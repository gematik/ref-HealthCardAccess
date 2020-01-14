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

import java.util.HashMap;
import java.util.Map;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ValueStateChecker;

/**
 * Commands representing Manage Channel command in gemSpec_COS#14.9.8
 */
public class ManageChannelCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x70;

    private static final int MODE_INTENDED_ACTION_OPEN_CHANNEL_P1 = 0x00;
    private static final int MODE_INTENDED_ACTION_OPEN_CHANNEL_P2 = 0x00;
    private static final int MODE_INTENDED_RESET_ON_APPLICATION_LEVEL_P1 = 0x40;
    private static final int MODE_INTENDED_RESET_ON_APPLICATION_LEVEL_P2 = 0x01;
    private static final int MODE_INTENDED_ACTION_CLOSE_CHANNEL_P1 = 0x80;
    private static final int MODE_INTENDED_RESET_CLOSE_CHANNEL_P1 = 0x40;
    private static final int MODE_AFFECTED_CHANNEL_IN_CLA_BYTE = 0x00;
    private static final int EXPECTED_LENGTH_01 = 0x01;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.NO_MORE_CHANNELS_AVAILABLE);//NOCS(LAC): Response Code
    }

    /**
     * Use Case 14.9.8.1: Open a logical channel + Use Case 14.9.8.4: Logical Reset of application level
     * 
     * @param ifTrueOpenNewChannelElseResetOnApplicationLevel
     *            if true, open a new channel, if false execute logical reset on application level
     */
    public ManageChannelCommand(final boolean ifTrueOpenNewChannelElseResetOnApplicationLevel) {
        super(CLA, INS);
        if (ifTrueOpenNewChannelElseResetOnApplicationLevel) {
            this.p1 = MODE_INTENDED_ACTION_OPEN_CHANNEL_P1;
            this.p2 = MODE_INTENDED_ACTION_OPEN_CHANNEL_P2;
            this.ne = EXPECTED_LENGTH_01;
        } else {
            this.p1 = MODE_INTENDED_RESET_ON_APPLICATION_LEVEL_P1;
            this.p2 = MODE_INTENDED_RESET_ON_APPLICATION_LEVEL_P2;
        }
    }

    /**
     * Use Case 14.9.8.2: Close a logical channel + Use Case 14.9.8.3: Reset a logical channel
     *
     * @param logicalChannelNumber
     * @param ifTrueCloseChannelElseResetChannel
     */
    public ManageChannelCommand(final int logicalChannelNumber, final boolean ifTrueCloseChannelElseResetChannel) {
        super(logicalChannelNumber, INS);
        if (ifTrueCloseChannelElseResetChannel) {
            this.p1 = MODE_INTENDED_ACTION_CLOSE_CHANNEL_P1;
        } else {
            this.p1 = MODE_INTENDED_RESET_CLOSE_CHANNEL_P1;
        }
        this.p2 = MODE_AFFECTED_CHANNEL_IN_CLA_BYTE;

        ISanityChecker<Boolean> checker = ValueStateChecker.getInstance();
        checker.setMsgIncaseError("ManageChannelCommand.errMsg").check(logicalChannelNumber != 0);

    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
