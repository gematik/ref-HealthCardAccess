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

/**
 * Commands representing Compute Digital Signature in gemSpec_COS#14.8.2
 */
public class PsoComputeDigitalSignatureCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x2A;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6400, Response.ResponseStatus.KEY_INVALID);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_KEY_REFERENCE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);//NOCS(LAC): Response Code
    }

    private static final PsoAlgorithm.Algorithm[] VALID_PSOALG = { PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_CLIENT_AUTHENTICATION,
            PsoAlgorithm.Algorithm.SIGN_VERIFY_ECDSA,
            PsoAlgorithm.Algorithm.SIGN_VERIFY_SIGNPKCS1_V1_5, PsoAlgorithm.Algorithm.SIGN_VERIFY_PSS };

    /**
     * Use Case 14.8.2.1: Sign data field, no "message recovery"
     *
     * @param psoAlgorithm
     * @param dataToBeSigned
     */
    public PsoComputeDigitalSignatureCommand(final PsoAlgorithm psoAlgorithm, final byte[] dataToBeSigned) {
        super(CLA, INS);
        this.p1 = 0x9E;
        this.p2 = 0x9A;
        this.data = dataToBeSigned;
        this.ne = EXPECT_ALL_WILDCARD;

        ISanityChecker<byte[]> cmdDataChecker = CmdDataChecker.getInstance();
        cmdDataChecker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoComputeDigitalSignatureCommand.class)
                .setCurrentParameter(psoAlgorithm).check(data);

        // gemSpec_COS#N017.600 + 018.300
        ISanityChecker<Enum> enumsValidationChecker = EnumsValidationChecker.getInstance();
        enumsValidationChecker.setSpecifiedValues(VALID_PSOALG).setMsgIncaseError("PsoComputeDigitalSignatureCommand.errMsg")
                .check(psoAlgorithm.getAlgorithm());

    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
