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
import de.gematik.ti.healthcardaccess.sanitychecker.CmdDataChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.IntegerRangeChecker;

/**
 * Commands representing Update Binary command in gemSpec_COS#14.3.5
 */
public class UpdateCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0xD6;

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
        RESPONSE_MESSAGES.put(0x6A84, Response.ResponseStatus.DATA_TOO_BIG);//NOCS(LAC): Response Code
    }
    private final ISanityChecker<Integer> checkerIntegerValue = IntegerRangeChecker.getInstance();
    private final ISanityChecker<byte[]> checkerCmdData = CmdDataChecker.getInstance();

    /**
     * Convenience constructor calls UpdateCommand(0, data)
     *
     * @param data
     */
    public UpdateCommand(final byte[] data) {
        this(0, data);
    }

    /**
     * Use case Update Binary without shortFileIdentifier gemSpec_COS#14.3.5.1
     *
     * @param offset
     * @param data
     */
    public UpdateCommand(final int offset, final byte[] data) {
        super(CLA, INS);
        this.p2 = offset % BYTE_MODULO;
        this.p1 = (offset - this.p2) / BYTE_MODULO;
        this.data = data;

        checkerIntegerValue.setMsgIncaseError("Offset.errMsg")
                .setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_OFFSET_RANGE)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_OFFSET_WITHOUT_SFI_RANGE)
                .check(offset);
    }

    /**
     * Convenience constructor calls UpdateCommand(sfi, 0, data)
     *
     * @param sfi
     * @param data
     */
    public UpdateCommand(final ShortFileIdentifier sfi, final byte[] data) {
        this(sfi, 0, data);
    }

    /**
     * Use case Update Binary without shortFileIdentifier gemSpec_COS#14.3.5.2
     *
     * @param sfi
     * @param offset
     * @param data
     */
    public UpdateCommand(final ShortFileIdentifier sfi, final int offset, final byte[] data) {
        super(CLA, INS);
        this.p1 = SFI_MARKER + sfi.getSfId();
        this.p2 = offset;
        this.data = data;

        checkerIntegerValue.setMsgIncaseError("Offset.errMsg")
                .setSpecialConfigurationPair("minValue", IntegerRangeChecker.MIN_OFFSET_RANGE)
                .setSpecialConfigurationPair("maxValue", IntegerRangeChecker.MAX_OFFSET_WITH_SFI_RANGE)
                .check(offset);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
