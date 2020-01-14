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
 * Commands representing Read Record command in gemSpec_COS#14.4.6
 */
public class ReadRecordCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0xB2;
    private static final int MODE_USE_LIST_ELEMENT_P1 = 0x04;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6281, Response.ResponseStatus.CORRUPT_DATA_WARNING);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6282, Response.ResponseStatus.END_OF_RECORD_WARNING);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6287, Response.ResponseStatus.RECORD_DEACTIVATED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.WRONG_FILE_TYPE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6986, Response.ResponseStatus.NO_CURRENT_EF);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A82, Response.ResponseStatus.FILE_NOT_FOUND);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A83, Response.ResponseStatus.RECORD_NOT_FOUND);//NOCS(LAC): Response Code
    }
    private final ISanityChecker<Integer> checker = IntegerRangeChecker.getInstance();

    /**
     * Convenience constructor calls ReadRecordCommand(recordNumber, EXPECT_ALL_WILDCARD)
     *
     * @param recordNumber
     */
    public ReadRecordCommand(final int recordNumber) {
        this(recordNumber, EXPECT_ALL_WILDCARD);
    }

    /**
     * Use case Read Record without shortFileIdentifier gemSpec_COS#14.4.6.1
     *
     * @param recordNumber
     * @param ne
     */
    public ReadRecordCommand(final int recordNumber, final int ne) {
        super(CLA, INS);
        this.p1 = recordNumber;
        this.p2 = MODE_USE_LIST_ELEMENT_P1;
        this.ne = ne;
        checker.setMsgIncaseError("RecordNumber.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(recordNumber);
    }

    /**
     * Convenience constructor calls ReadRecordCommand(sfi, recordNumber, EXPECT_ALL_WILDCARD)
     *
     * @param sfi
     * @param recordNumber
     */
    public ReadRecordCommand(final ShortFileIdentifier sfi, final int recordNumber) {
        this(sfi, recordNumber, EXPECT_ALL_WILDCARD);
    }

    /**
     * Use case Read Record with shortFileIdentifier gemSpec_COS#14.4.6.2
     *
     * @param sfi
     * @param recordNumber
     * @param ne
     */
    public ReadRecordCommand(final ShortFileIdentifier sfi, final int recordNumber, final int ne) {
        super(CLA, INS);
        this.p1 = recordNumber;
        this.p2 = (sfi.getSfId() << 3) + MODE_USE_LIST_ELEMENT_P1;
        this.ne = ne;
        checker.setMsgIncaseError("RecordNumber.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(recordNumber);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
