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

/**
 * Commands representing Load Application Command gemSpec_COS#14.2.5
 */
public class LoadApplicationCommand extends AbstractHealthCardCommand {
    private static final int CLA_COMMAND_CHAINING = 0x10;
    private static final int CLA_NO_COMMAND_CHAINING = 0x00;
    private static final int INS = 0xEA;
    private static final int NO_MEANING = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_00);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_01);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_02);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_03);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6581, Response.ResponseStatus.MEMORY_FAILURE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A84, Response.ResponseStatus.OUT_OF_MEMORY);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A89, Response.ResponseStatus.DUPLICATED_OBJECTS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A8A, Response.ResponseStatus.DF_NAME_EXISTS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6D00, Response.ResponseStatus.INSTRUCTION_NOT_SUPPORTED);//NOCS(LAC): Response Code
    }

    /**
     * Use cases Load Application gemSpec_Cos#14.2.5.1 - 14.2.5.2
     *
     * @param data
     * @param commandChaining
     *            true when not last command in chain, false when last command
     */
    public LoadApplicationCommand(final byte[] data, final boolean commandChaining) {
        super(commandChaining ? CLA_COMMAND_CHAINING : CLA_NO_COMMAND_CHAINING, INS);
        this.p1 = NO_MEANING;
        this.p2 = NO_MEANING;
        this.data = data;

        // TODO:
        // ISanityChecker checker = CmdDataChecker.getInstance();
        // IntegerRangeChecker ?
        // checker.setMsgIncaseError("LoadApplicationCommand.errMsg").setSpecialConfigurationPair("tlvPattern", true).check(data);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
