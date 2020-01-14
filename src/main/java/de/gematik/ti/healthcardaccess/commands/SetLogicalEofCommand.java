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
 * Commands representing Set logical EOF command in gemSpec_COS#14.3.4
 */
public class SetLogicalEofCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x80;
    private static final int INS = 0xE0;

    private static final int BYTE_MODULO = 256;
    private static final int SFI_MARKER = 0x80;

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
        RESPONSE_MESSAGES.put(0x6986, Response.ResponseStatus.NO_CURRENT_EF);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A82, Response.ResponseStatus.FILE_NOT_FOUND);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6B00, Response.ResponseStatus.OFFSET_TOO_BIG);//NOCS(LAC): Response Code
    }
    private final ISanityChecker<Integer> checker = IntegerRangeChecker.getInstance();

    /**
     * Convenience constructor calls SetLogicalEofCommand(0)
     */
    public SetLogicalEofCommand() {
        this(0);
    }

    /**
     * Use case Set logical EOF without shortFileIdentifier gemSpec_COS#14.3.4.1
     *
     * @param offset
     */
    public SetLogicalEofCommand(final int offset) {
        super(CLA, INS);
        this.p2 = offset % BYTE_MODULO;
        this.p1 = (offset - this.p2) / BYTE_MODULO;
        checker.setMsgIncaseError("Offset.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_OFFSET_RANGE)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_OFFSET_WITHOUT_SFI_RANGE)
                .check(offset);
    }


    /**
     * Convenience constructor calls SetLogicalEofCommand(sfi, 0)
     */
    public SetLogicalEofCommand(final ShortFileIdentifier sfi) {
        this(sfi, 0);
    }

    /**
     * Use case Set logical EOF with shortFileIdentifier gemSpec_COS#14.3.4.2
     *
     * @param sfi
     * @param offset
     */
    public SetLogicalEofCommand(final ShortFileIdentifier sfi, final int offset) {
        super(CLA, INS);
        this.p1 = SFI_MARKER + sfi.getSfId();
        this.p2 = offset;
        checker.setMsgIncaseError("Offset.errMsg").setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_OFFSET_RANGE)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_OFFSET_WITH_SFI_RANGE)
                .check(offset);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
