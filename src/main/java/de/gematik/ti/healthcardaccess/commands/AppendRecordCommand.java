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
import de.gematik.ti.healthcardaccess.cardobjects.ShortFileIdentifier;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.IntegerRangeChecker;

/**
 * Commands representing Append Record command in gemSpec_COS#14.4.2
 */
public class AppendRecordCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0xE2;
    private static final int NO_MEANING = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_00);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_01);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_02);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_03);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6581, Response.ResponseStatus.MEMORY_FAILURE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6700, Response.ResponseStatus.WRONG_RECORD_LENGTH);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.WRONG_FILE_TYPE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6986, Response.ResponseStatus.NO_CURRENT_EF);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A82, Response.ResponseStatus.FILE_NOT_FOUND);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A84, Response.ResponseStatus.FULL_RECORD_LIST);//NOCS(LAC): Response Code
    }
    private final ISanityChecker<Integer> checker = IntegerRangeChecker.getInstance();

    /**
     * Use case Append Record without shortFileIdentifier gemSpec_COS#14.4.2.1
     *
     * @param data
     */
    public AppendRecordCommand(final byte[] data) {
        super(CLA, INS);
        this.p1 = NO_MEANING;
        this.p2 = NO_MEANING;
        this.data = data;

        checker.setMsgIncaseError("RecordData.errMsg")
                .setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(data.length);
    }

    /**
     * Use case Append Record with shortFileIdentifier gemSpec_COS#14.4.2.2
     *
     * @param sfi
     * @param data
     */
    public AppendRecordCommand(final ShortFileIdentifier sfi, final byte[] data) {
        super(CLA, INS);
        this.p1 = NO_MEANING;
        this.p2 = (sfi.getSfId() << 3);
        this.data = data;

        checker.setMsgIncaseError("RecordData.errMsg")
                .setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(data.length);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
