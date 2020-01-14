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
 * Commands representing Activate Record command in gemSpec_COS#14.4.1
 */
public class ActivateRecordCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x08;
    private static final int MODE_USE_LIST_ELEMENT_P1 = 0x04;
    private static final int MODE_USE_ALL_LIST_ELEMENTS_STARTING_FROM_P1 = 0x05;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_00);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_01);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_02);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_03);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6581, Response.ResponseStatus.MEMORY_FAILURE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.WRONG_FILE_TYPE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_RECORD_LIFE_CYCLE_STATUS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6986, Response.ResponseStatus.NO_CURRENT_EF);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A82, Response.ResponseStatus.FILE_NOT_FOUND);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A83, Response.ResponseStatus.RECORD_NOT_FOUND);//NOCS(LAC): Response Code
    }
    private final ISanityChecker checker = IntegerRangeChecker.getInstance();

    /**
     * Use cases Activate Record without shortFileIdentifier gemSpec_COS#14.4.1.1 + 14.4.1.3
     *
     * @param recordNumber
     * @param activateAllRecordsStartingFromRecordNumber
     */
    public ActivateRecordCommand(final int recordNumber, final boolean activateAllRecordsStartingFromRecordNumber) {
        // activate record(s) in currently selected file
        super(CLA, INS);
        this.p1 = recordNumber;
        this.p2 = activateAllRecordsStartingFromRecordNumber ? MODE_USE_ALL_LIST_ELEMENTS_STARTING_FROM_P1 : MODE_USE_LIST_ELEMENT_P1;

        checker.setMsgIncaseError("RecordNumber.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(recordNumber);
    }

    /**
     * Use cases Activate Record with shortFileIdentifier gemSpec_COS#14.4.1.2 + 14.4.1.4
     *
     * @param recordNumber
     * @param activateAllRecordsStartingFromRecordNumber
     */
    public ActivateRecordCommand(final ShortFileIdentifier sfi, final int recordNumber, final boolean activateAllRecordsStartingFromRecordNumber) {
        super(CLA, INS);
        this.p1 = recordNumber;
        this.p2 = (sfi.getSfId() << 3) + (activateAllRecordsStartingFromRecordNumber ? MODE_USE_ALL_LIST_ELEMENTS_STARTING_FROM_P1 : MODE_USE_LIST_ELEMENT_P1);
        checker.setMsgIncaseError("RecordNumber.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(recordNumber);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
