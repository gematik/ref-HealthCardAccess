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

/**
 * Commands representing List Public Key command gemSpec_COS#14.9.7
 */
public class ListPublicKeyCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x80;
    private static final int INS = 0xCA;

    private static final int MODE_ALL_KIND_OF_PUBLIC_KEY_OBJECTS_P1 = 0x01;
    private static final int MODE_ALL_KIND_OF_PUBLIC_KEY_OBJECTS_P2 = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6200, Response.ResponseStatus.DATA_TRUNCATED);//NOCS(LAC): Response Code
    }

    /**
     * List all Public Keys - gemSpec_COS#14.9.7
     */
    public ListPublicKeyCommand() {
        super(CLA, INS);
        this.p1 = MODE_ALL_KIND_OF_PUBLIC_KEY_OBJECTS_P1;
        this.p2 = MODE_ALL_KIND_OF_PUBLIC_KEY_OBJECTS_P2;
        this.ne = EXPECT_ALL_WILDCARD;
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
