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
import de.gematik.ti.healthcardaccess.cardobjects.PsoAlgorithm;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.CmdDataChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.EnumsValidationChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import de.gematik.ti.utils.primitives.Bytes;

/**
 * Commands representing Compute Cryptographic Checksum in gemSpec_COS#14.8.1
 */
public class PsoComputeCryptographicChecksum extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x2A;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_KEY_REFERENCE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);//NOCS(LAC): Response Code
    }

    /**
     * Use Case 14.8.1.1: Use case Compute Cryptographic Checksum
     *
     * @param psoAlgorithm
     * @param data: plain data
     */
    public PsoComputeCryptographicChecksum(final PsoAlgorithm psoAlgorithm, final byte[] data) {
        super(CLA, INS);
        this.p1 = 0x8E;
        this.p2 = 0x80;

        // gemSpec_COS#N087.220
        ISanityChecker<Enum> checker = EnumsValidationChecker.getInstance();
        checker.setSpecifiedValues(PsoAlgorithm.Algorithm.DE_ENCRYPT_AES_SESSIONKEY, PsoAlgorithm.Algorithm.DE_ENCRYPT_DES_SESSIONKEY_OPTION_DES)
                .setMsgIncaseError("PsoComputeCryptographicChecksum.errMsg").check(psoAlgorithm.getAlgorithm());

        byte[] sscMacIncrement;
        if (PsoAlgorithm.Algorithm.DE_ENCRYPT_AES_SESSIONKEY == psoAlgorithm.getAlgorithm()) {
            // gemSpec_COS#N087.220
            if (psoAlgorithm.isFlagSscMacIncrement()) {
                sscMacIncrement = new byte[] { 0x01 };
            } else {
                sscMacIncrement = new byte[] { 0x00 };
            }
        } else { // == PsoAlgorithm.Algorithm.DE_ENCRYPT_DES_SESSIONKEY
            sscMacIncrement = null;
        }
        this.data = Bytes.concatNullables(sscMacIncrement, data);

        this.ne = EXPECT_ALL_WILDCARD;

        ISanityChecker<byte[]> cmdDataChecker = CmdDataChecker.getInstance();
        cmdDataChecker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoComputeCryptographicChecksum.class)
                .setCurrentParameter(psoAlgorithm).check(this.data);

    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
