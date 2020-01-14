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
import de.gematik.ti.healthcardaccess.cardobjects.GemCvCertificate;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.CmdDataChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;

/**
 * Commands representing Verify Certificate in gemSpec_COS#14.8.7
 */
public class PsoVerifyCertificateCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x2A;
    private static final int NO_RESPONSE_DATA_P1 = 0x00;
    private static final int COMMAND_DATA_WITH_TEMPLATE_WITH_CERTIFIED_FIELDS_P1 = 0xAE;
    private static final int COMMAND_DATA_WITH_CERTIFIED_TEMPLATE_P2 = 0xBE;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_00);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_01);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_02);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_03);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.MEMORY_FAILURE);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6983, Response.ResponseStatus.KEY_EXPIRED);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_KEY_REFERENCE);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A80, Response.ResponseStatus.WRONG_CIPHER_TEXT);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.WRONG_KEY_REFERENCE);// NOCS(LAC): Response Code
    }

    /**
     * Use Case 14.8.7.2: Import of ELC-key by certificate
     * 
     * @param gemCvCertificate
     */
    public PsoVerifyCertificateCommand(final GemCvCertificate gemCvCertificate) {
        super(CLA, INS);
        this.p1 = NO_RESPONSE_DATA_P1;
        this.p2 = COMMAND_DATA_WITH_CERTIFIED_TEMPLATE_P2;
        this.data = gemCvCertificate.getCertificateContent();

        ISanityChecker<byte[]> checker = CmdDataChecker.getInstance();
        checker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoVerifyCertificateCommand.class)
                .check(data);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
