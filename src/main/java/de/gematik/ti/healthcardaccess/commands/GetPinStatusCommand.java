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
import de.gematik.ti.healthcardaccess.cardobjects.Password;
import de.gematik.ti.healthcardaccess.result.Response;

/**
 * Command representing Get Pin Status Command gemSpec_COS#14.6.4
 */
public class GetPinStatusCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x80;
    private static final int INS = 0x20;

    private static final int NO_MEANING = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x62C1, Response.ResponseStatus.TRANSPORT_STATUS_TRANSPORT_PIN);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x62C7, Response.ResponseStatus.TRANSPORT_STATUS_EMPTY_PIN);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x62D0, Response.ResponseStatus.PASSWORD_DISABLED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.RETRY_COUNTER_COUNT_00);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.RETRY_COUNTER_COUNT_01);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.RETRY_COUNTER_COUNT_02);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.RETRY_COUNTER_COUNT_03);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6988, Response.ResponseStatus.PASSWORD_NOT_FOUND);//NOCS(LAC): Response Code
    }

    /**
     * Use case Get Pin Status gemSpec_COS#14.6.4.1
     *
     * @param password the arguments for the Get Pin Status command
     * @param dfSpecific whether or not the password object specifies a Global or DF-specific.
     *                     true = DF-Specific, false = global
     */
    public GetPinStatusCommand(final Password password, final boolean dfSpecific) {
        super(CLA, INS);
        this.p1 = NO_MEANING;
        this.p2 = password.calculateKeyReference(dfSpecific);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
