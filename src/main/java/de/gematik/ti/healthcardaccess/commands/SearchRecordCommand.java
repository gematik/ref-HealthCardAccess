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
 * Commands representing Search Record command in gemSpec_COS#14.4.7
 */
public class SearchRecordCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0xA2;
    private static final int MODE_USE_LIST_ELEMENT_P1_AND_FOLLOWING = 0x04;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    private static final short WILDCARDSHORT = 256;

    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6281, Response.ResponseStatus.CORRUPT_DATA_WARNING);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6282, Response.ResponseStatus.END_OF_RECORD_WARNING);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.WRONG_FILE_TYPE);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6986, Response.ResponseStatus.NO_CURRENT_EF);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A82, Response.ResponseStatus.FILE_NOT_FOUND);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A83, Response.ResponseStatus.RECORD_NOT_FOUND);// NOCS(LAC): Response Code
    }
    private final ISanityChecker<Integer> checker = IntegerRangeChecker.getInstance();

    /**
     * Convenience constructor calls SearchRecordCommand(recordNumber, searchString, WILDCARDSHORT)
     *
     * @param recordNumber
     * @param searchString
     */
    public SearchRecordCommand(final int recordNumber, final byte[] searchString) {
        this(recordNumber, searchString, WILDCARDSHORT);
    }

    /**
     * Use case Search Record without shortFileIdentifier gemSpec_COS#14.4.7.1
     *
     * @param recordNumber
     * @param searchString
     * @param ne
     */
    public SearchRecordCommand(final int recordNumber, final byte[] searchString, final int ne) {
        super(CLA, INS);
        this.p1 = recordNumber;
        this.p2 = MODE_USE_LIST_ELEMENT_P1_AND_FOLLOWING;
        this.data = searchString;
        this.ne = ne;
        checker.setMsgIncaseError("RecordNumber.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(recordNumber);
        checker.setMsgIncaseError("RecordSearchData.errMsg")
                .setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_SEARCH_STRING_LENGTH)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_SEARCH_STRING_LENGTH)
                .check(searchString.length);
    }

    /**
     * Convenience constructor calls SearchRecordCommand(sfi, recordNumber, searchString, WILDCARDSHORT)
     *
     * @param sfi
     * @param recordNumber
     * @param searchString
     */
    public SearchRecordCommand(final ShortFileIdentifier sfi, final int recordNumber, final byte[] searchString) {
        this(sfi, recordNumber, searchString, WILDCARDSHORT);
    }

    /**
     * Use case Search Record without shortFileIdentifier gemSpec_COS#14.4.7.1
     *
     * @param sfi
     * @param recordNumber
     * @param searchString
     * @param ne
     */
    public SearchRecordCommand(final ShortFileIdentifier sfi, final int recordNumber, final byte[] searchString, final int ne) {
        super(CLA, INS);
        this.p1 = recordNumber;
        this.p2 = (sfi.getSfId() << 3) + MODE_USE_LIST_ELEMENT_P1_AND_FOLLOWING;
        this.data = searchString;
        this.ne = ne;
        checker.setMsgIncaseError("RecordNumber.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_NUMBER)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_NUMBER)
                .check(recordNumber);
        checker.setMsgIncaseError("RecordSearchData.errMsg")
                .setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_RECORD_SEARCH_STRING_LENGTH)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_RECORD_SEARCH_STRING_LENGTH)
                .check(searchString.length);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
