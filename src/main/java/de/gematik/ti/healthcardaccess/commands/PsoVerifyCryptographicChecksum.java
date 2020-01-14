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
import de.gematik.ti.healthcardaccess.sanitychecker.ValueStateChecker;
import de.gematik.ti.utils.primitives.Bytes;

/**
 * Commands representing Verify Cryptographic Checksum command in gemSpec_COS#14.8.8
 */
public class PsoVerifyCryptographicChecksum extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x2A;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_KEY_REFERENCE);
        RESPONSE_MESSAGES.put(0x6A80, Response.ResponseStatus.VERIFICATION_ERROR);
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

    /**
     * Use Case 14.8.8: Use case PSO Verify Cryptographic Checksum
     *
     * @param psoAlgorithm
     * @param data: plain data
     * @param mac: checksum
     */
    public PsoVerifyCryptographicChecksum(final PsoAlgorithm psoAlgorithm, byte[] data, byte[] mac) {
        super(CLA, INS);
        this.p1 = 0x00;
        this.p2 = 0xA2;

        // (N002.700)e / (N002.810)h / (N002.900)b / (N003.010)b or (N003.020)b
        ISanityChecker valueStateChecker = ValueStateChecker.getInstance();
        valueStateChecker.setMsgIncaseError("PsoVerifyCryptographicChecksum.MAC.errMsg").check(mac.length == 8);

        // gemSpec_COS#N087.220
        ISanityChecker<Enum> checker = EnumsValidationChecker.getInstance();
        checker.setSpecifiedValues(PsoAlgorithm.Algorithm.DE_ENCRYPT_AES_SESSIONKEY, PsoAlgorithm.Algorithm.DE_ENCRYPT_DES_SESSIONKEY_OPTION_DES)
                .setMsgIncaseError("PsoVerifyCryptographicChecksum.errMsg").check(psoAlgorithm.getAlgorithm());

        this.data = Bytes.concatNullables(new byte[] { (byte) 0x80, (byte) data.length }, data, new byte[] { (byte) 0x8E, (byte) mac.length }, mac);

        ISanityChecker<byte[]> cmdDataChecker = CmdDataChecker
                .getInstance();
        cmdDataChecker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoVerifyCryptographicChecksum.class)
                .setCurrentParameter(psoAlgorithm).check(data);

    }
}
