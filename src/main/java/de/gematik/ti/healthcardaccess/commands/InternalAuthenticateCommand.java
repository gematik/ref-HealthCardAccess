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
 * Commands representing Internal Authentication command in gemSpec_COS#14.7.4
 */
public class InternalAuthenticateCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x88;
    private static final int ALGORITHM_INFORMATION_PRESENT = 0x00;
    private static final int KEY_REFERENCE_PRESENT_ON_CARD = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    private static final PsoAlgorithm.Algorithm[] VALID_PSOALGO = {
            PsoAlgorithm.Algorithm.AUTHENTICATE_ELC_ASYNCHRON_ADMIN, PsoAlgorithm.Algorithm.AUTHENTICATE_ELC_ROLE_AUTHENTICATION,
            PsoAlgorithm.Algorithm.AUTHENTICATE_ELC_SESSIONKEY_4SM, PsoAlgorithm.Algorithm.AUTHENTICATE_ELC_SESSIONKEY_4TC,
            PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_CLIENT_AUTHENTICATION, PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_ROLE_AUTHENTICATION_OPTION_CVC,
            PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_SESSIONKEY_4SM_OPTION_DES, PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_SESSIONKEY_4TC_OPTION_DES,
            PsoAlgorithm.Algorithm.SIGN_VERIFY_SIGNPKCS1_V1_5
    };
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6400, Response.ResponseStatus.KEY_INVALID);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_KEY_REFERENCE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A80, Response.ResponseStatus.WRONG_TOKEN);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_OR_PRK_NOT_FOUND);//NOCS(LAC): Response Code
    }

    private final ISanityChecker[] sanityChecker = { EnumsValidationChecker.getInstance(), CmdDataChecker.getInstance() };

    /**
     * Use case Internal Authentication - gemSpec_COS#14.7.4.1
     *
     * @param psoAlgorithm
     * @param token
     */
    public InternalAuthenticateCommand(final PsoAlgorithm psoAlgorithm, final byte[] token) {
        super(CLA, INS);
        this.p1 = ALGORITHM_INFORMATION_PRESENT;
        this.p2 = KEY_REFERENCE_PRESENT_ON_CARD;
        this.data = token;
        this.ne = EXPECT_ALL_WILDCARD;

        ISanityChecker<byte[]> cmdDataChecker = CmdDataChecker.getInstance();
        cmdDataChecker.setMsgIncaseError("CmdDataLength.errMsg")
                .setCurrentParameter(InternalAuthenticateCommand.class)
                .setCurrentParameter(psoAlgorithm.getAlgorithm())
                .check(token);

        ISanityChecker<Enum> enumsValidationChecker = EnumsValidationChecker.getInstance();
        enumsValidationChecker.setMsgIncaseError("InternalAuthenticateCommand.errMsg").setSpecifiedValues(VALID_PSOALGO).check(psoAlgorithm.getAlgorithm());

    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
