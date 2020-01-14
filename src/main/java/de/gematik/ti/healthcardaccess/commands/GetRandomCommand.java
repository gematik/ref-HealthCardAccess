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
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.IntegerRangeChecker;

/**
 * Commands representing Get Random command in gemSpec_COS#14.9.5
 */
public class GetRandomCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x80;
    private static final int INS = 0x84;

    private static final int NO_MEANING = 0x00;
    private static final int EXPECTED_LENGTH_MIN = 0;
    private static final int EXPECTED_LENGTH_MAX = 255;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
    }

    /**
     * Get random bytes with given length - gemSpec_COS#14.9.5
     *
     * @param numberOfExpectedOctetsInResponse
     */
    public GetRandomCommand(final int numberOfExpectedOctetsInResponse) {
        super(CLA, INS);
        this.p1 = NO_MEANING;
        this.p2 = NO_MEANING;

        // gemSpec_COS#N099.320
        ISanityChecker<Integer> checker = IntegerRangeChecker.getInstance();
        checker.setSpecialConfigurationPair("minValue", EXPECTED_LENGTH_MIN).setSpecialConfigurationPair("maxValue", EXPECTED_LENGTH_MAX)
                .setMsgIncaseError("GetRandomCommand.errMsg")
                .check(numberOfExpectedOctetsInResponse);
        // {1, 2, …, 255, WildCardShort}
        this.ne = numberOfExpectedOctetsInResponse;
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
