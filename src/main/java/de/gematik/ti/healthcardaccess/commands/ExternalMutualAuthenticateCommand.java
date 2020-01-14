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
 * Commands representing the Authenticate commands in gemSpec_COS#14.7.2
        */
public class ExternalMutualAuthenticateCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x82;
    private static final int ALGORITHM_INFORMATION_PRESENT_P1 = 0x00;
    private static final int KEY_REFERENCE_PRESENT_ON_CARD_P2 = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();

    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6300, Response.ResponseStatus.AUTHENTICATION_FAILURE);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6983, Response.ResponseStatus.KEY_EXPIRED);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.WRONG_RANDOM_OR_NO_KEY_REFERENCE);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_OR_PRK_NOT_FOUND);// NOCS(LAC): Response Code
    }

    private static final PsoAlgorithm.Algorithm[] VALID_PSOALGO = {
            PsoAlgorithm.Algorithm.AUTHENTICATE_ELC_ROLE_CHECK, PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_ROLE_CHECK_OPTION_CVC,
            PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_SESSIONKEY_4TC_OPTION_DES, PsoAlgorithm.Algorithm.AUTHENTICATE_AES_SESSIONKEY_4TC,
            PsoAlgorithm.Algorithm.AUTHENTICATE_DES_SESSIONKEY_4TC, PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_SESSIONKEY_4SM_OPTION_DES,
            PsoAlgorithm.Algorithm.AUTHENTICATE_AES_SESSIONKEY_4SM, PsoAlgorithm.Algorithm.AUTHENTICATE_DES_SESSIONKEY_4SM
    };

    /**
     * Use Case 14.7.1.1 External Authentication without response data + 14.7.1.2 External Authentication with response data
     *
     * @param psoAlgorithm
     * @param cmdData
     * @param expectResponseData
     *            - false for External Authenticate, true for Mutual Authenticate
     */
    public ExternalMutualAuthenticateCommand(final PsoAlgorithm psoAlgorithm, final byte[] cmdData, final boolean expectResponseData) {
        super(CLA, INS);
        this.p1 = ALGORITHM_INFORMATION_PRESENT_P1;
        this.p2 = KEY_REFERENCE_PRESENT_ON_CARD_P2;
        this.data = cmdData;
        if (expectResponseData) {
            this.ne = EXPECT_ALL_WILDCARD;
        }

        ISanityChecker<byte[]> cmdDataChecker = CmdDataChecker.getInstance();
        cmdDataChecker.setMsgIncaseError("CmdDataLength.errMsg").setCurrentParameter(psoAlgorithm.getAlgorithm())
                .setCurrentParameter(ExternalMutualAuthenticateCommand.class)
                .check(cmdData);

        ISanityChecker<Enum> enumsValidationChecker = EnumsValidationChecker.getInstance();
        enumsValidationChecker.setMsgIncaseError("ExternalMutualAuthenticateCommand.errMsg").setSpecifiedValues(VALID_PSOALGO)
                .check(psoAlgorithm.getAlgorithm());
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
