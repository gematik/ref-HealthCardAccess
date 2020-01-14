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
 * Commands representing Decipher command in gemSpec_COS#14.8.3
 */
public class PsoDecipher extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x2A;
    private static final int P1 = 0x80;
    private static final int P2 = 0x86;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6400, Response.ResponseStatus.KEY_INVALID);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_KEY_REFERENCE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A80, Response.ResponseStatus.WRONG_CIPHER_TEXT);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);//NOCS(LAC): Response Code
    }

    private static final PsoAlgorithm.Algorithm[] VALID_PSOALGO = { PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_DECIPHER_PKCS1_V1_5,
            PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_DECIPHER_OAEP,
            PsoAlgorithm.Algorithm.DE_ENCRYPT_ELC_SHARED_SECRET_CALCULATION, PsoAlgorithm.Algorithm.DE_ENCRYPT_AES_SESSIONKEY,
            PsoAlgorithm.Algorithm.DE_ENCRYPT_DES_SESSIONKEY_OPTION_DES,
    };

    /**
     * Use Case 14.8.3.1: Decipher by RSA + 14.8.3.2: Decipher by ELC + 14.8.3.3: Decipher by symmetric key
     *
     * @param psoAlgorithm
     * @param dataToBeDeciphered
     */
    public PsoDecipher(final PsoAlgorithm psoAlgorithm, final byte[] dataToBeDeciphered) {
        super(CLA, INS, P1, P2);

        ISanityChecker<Enum> enumsValidationChecker = EnumsValidationChecker.getInstance();
        enumsValidationChecker.setSpecifiedValues(VALID_PSOALGO).setMsgIncaseError("PsoDecipher.errMsg").check(psoAlgorithm.getAlgorithm());

        if (PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_DECIPHER_PKCS1_V1_5 == psoAlgorithm.getAlgorithm()
                || PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_DECIPHER_OAEP == psoAlgorithm.getAlgorithm()) {
            // gemSpec_COS#14.8.3.1 Decipher by RSA
            this.data = Bytes.concatNullables(new byte[] { 0x00 }, dataToBeDeciphered);
            this.ne = EXPECT_ALL_WILDCARD;
        } else if (PsoAlgorithm.Algorithm.DE_ENCRYPT_ELC_SHARED_SECRET_CALCULATION == psoAlgorithm.getAlgorithm()) {
            // gemSpec_COS#14.8.3.2 Decipher by Elliptic Curves
            this.data = dataToBeDeciphered;
            this.ne = EXPECT_ALL_WILDCARD;
        } else if (PsoAlgorithm.Algorithm.DE_ENCRYPT_AES_SESSIONKEY == psoAlgorithm.getAlgorithm()
                || PsoAlgorithm.Algorithm.DE_ENCRYPT_DES_SESSIONKEY_OPTION_DES == psoAlgorithm.getAlgorithm()) {
            // gemSpec_COS#14.8.3.3 Decipher by symmetric key
            this.data = Bytes.concatNullables(new byte[] { 0x01 }, dataToBeDeciphered);
            this.ne = EXPECT_ALL_WILDCARD;
        }

        ISanityChecker<byte[]> cmdDataChecker = CmdDataChecker.getInstance();
        cmdDataChecker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoDecipher.class)
                .check(data);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
