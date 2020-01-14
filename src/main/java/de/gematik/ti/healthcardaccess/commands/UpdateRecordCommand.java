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
 * Commands representing Update Record command in gemSpec_COS#14.4.8
 */
public class UpdateRecordCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0xDC;
    private static final int MODE_USE_LIST_ELEMENT_P1 = 0x04;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_00);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_01);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_02);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_03);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6287, Response.ResponseStatus.RECORD_DEACTIVATED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6581, Response.ResponseStatus.MEMORY_FAILURE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6700, Response.ResponseStatus.WRONG_RECORD_LENGTH);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.WRONG_FILE_TYPE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6986, Response.ResponseStatus.NO_CURRENT_EF);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A82, Response.ResponseStatus.FILE_NOT_FOUND);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A83, Response.ResponseStatus.RECORD_NOT_FOUND);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A84, Response.ResponseStatus.OUT_OF_MEMORY);//NOCS(LAC): Response Code
    }

    /**
     * Use case Update Record without shortFileIdentifier gemSpec_COS#14.4.8.1
     *
     * @param recordNumber
     * @param toWriteData
     */
    public UpdateRecordCommand(final int recordNumber, final byte[] toWriteData) {
        super(CLA, INS);
        this.p1 = recordNumber;
        this.p2 = MODE_USE_LIST_ELEMENT_P1;
        this.data = toWriteData;
        ISanityChecker<Integer> checker = IntegerRangeChecker.getInstance();
        checker.setMsgIncaseError("RecordNumber.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(recordNumber);
        checker.setMsgIncaseError("RecordData.errMsg")
                .setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_DATA_LENGTH)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_DATA_LENGTH)
                .check(toWriteData.length);
    }

    /**
     * Use case Update Record with shortFileIdentifier gemSpec_COS#14.4.8.2
     *
     * @param sfi
     * @param recordNumber
     * @param toWriteData
     */
    public UpdateRecordCommand(final ShortFileIdentifier sfi, final int recordNumber, final byte[] toWriteData) {
        super(CLA, INS);
        this.p1 = recordNumber;
        this.p2 = (sfi.getSfId() << 3) + MODE_USE_LIST_ELEMENT_P1;
        this.data = toWriteData;
        ISanityChecker<Integer> checker = IntegerRangeChecker.getInstance();
        checker.setMsgIncaseError("RecordNumber.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(recordNumber);
        checker.setMsgIncaseError("RecordData.errMsg")
                .setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_DATA_LENGTH)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_DATA_LENGTH)
                .check(toWriteData.length);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
