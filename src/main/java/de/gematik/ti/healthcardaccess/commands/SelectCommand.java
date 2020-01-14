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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.ApplicationIdentifier;
import de.gematik.ti.healthcardaccess.cardobjects.FileIdentifier;
import de.gematik.ti.healthcardaccess.result.Response;

/**
 * Commands representing Select Command gemSpec_COS#14.2.6
 */
public class SelectCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0xA4;
    private static final int SELECTION_MODE_DF_BY_FID = 0x01;
    private static final int SELECTION_MODE_EF_BY_FID = 0x02;
    private static final int SELECTION_MODE_PARENT = 0x03;
    private static final int SELECTION_MODE_AID = 0x04;
    private static final int RESPONSE_TYPE_NO_RESPONSE = 0x0C;
    private static final int RESPONSE_TYPE_FCP = 0x04;
    private static final int FILE_OCCURRENCE_FIRST = 0x00;
    private static final int FILE_OCCURRENCE_NEXT = 0x02;

    private static final int P2_FCP = 0x02;
    private static final int P2 = 0x0C;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6283, Response.ResponseStatus.FILE_DEACTIVATED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6285, Response.ResponseStatus.FILE_TERMINATED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A82, Response.ResponseStatus.FILE_NOT_FOUND);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6D00, Response.ResponseStatus.INSTRUCTION_NOT_SUPPORTED);//NOCS(LAC): Response Code
    }

    /**
     * Use case Select root of object system gemSpec_Cos#14.2.6.1 + use case Select parent folder gemSpec_Cos#14.2.6.11
     * Use case Select root of object system requesting File Control Parameter gemSpec_Cos#14.2.6.2 with Parameter readFirst true
     *
     * @param selectParentElseRoot if true SELECTION_MODE_PARENT else SELECTION_MODE_AID
     * @param readFirst if true read FCP else only select
     */
    public SelectCommand(final boolean selectParentElseRoot, boolean readFirst) {
        super(CLA, INS);
        this.p1 = selectParentElseRoot ? SELECTION_MODE_PARENT : SELECTION_MODE_AID;
        this.p2 = calculateP2(readFirst, false);
        this.ne = readFirst ? EXPECT_ALL_WILDCARD : null;
    }

    // Note: Left out use cases Select without Application Identifier, next gemSpec_Cos#14.2.6.3 - 14.2.6.4

    /**
     * Use case Select file with Application Identifier, first occurrence, no File Control Parameter gemSpec_Cos#14.2.6.5
     *
     * @param aid
     */
    public SelectCommand(final ApplicationIdentifier aid) {
        this(aid, false, false, 0);
    }

    /**
     * Use cases Select file with Application Identifier gemSpec_Cos#14.2.6.5 - 14.2.6.8
     * 
     * @param aid
     * @param selectNextElseFirstOccurrence
     * @param requestFcp
     * @param fcpLength,
     *            determine expected size of response if File Control Parameter requested
     */
    public SelectCommand(final ApplicationIdentifier aid, final boolean selectNextElseFirstOccurrence, final boolean requestFcp, final int fcpLength) {
        super(CLA, INS);
        this.p1 = SELECTION_MODE_AID;
        this.p2 = calculateP2(requestFcp, selectNextElseFirstOccurrence);
        this.data = Arrays.copyOf(aid.getAid(), aid.getAid().length);
        this.ne = requestFcp ? fcpLength : null;
    }

    /**
     * Use case Select DF with File Identifier gemSpec_Cos#14.2.6.9 and <br>
     * use case Select EF with File Identifier gemSpec_Cos#14.2.6.13
     *
     * @param fid
     */
    public SelectCommand(final FileIdentifier fid, final boolean selectDfElseEf) {
        this(fid, selectDfElseEf, false, 0);
    }

    /**
     * Use cases Select DF with File Identifier gemSpec_Cos#14.2.6.9 - 14.2.6.10 and <br>
     * use cases Select EF with File Identifier gemSpec_Cos#14.2.6.13 - 14.2.6.14
     *
     * @param fid
     * @param selectDfElseEf,
     *            true if Dedicated File shall be selected, false if Elementary File shall be selected
     * @param requestFcp
     * @param fcpLength,
     *            determine expected size of response if File Control Parameter requested
     */
    public SelectCommand(final FileIdentifier fid, final boolean selectDfElseEf, final boolean requestFcp, final int fcpLength) {
        super(CLA, INS);
        this.p1 = selectDfElseEf ? SELECTION_MODE_DF_BY_FID : SELECTION_MODE_EF_BY_FID;
        this.p2 = requestFcp ? P2_FCP : P2;
        this.data = Arrays.copyOf(fid.getFid(), fid.getFid().length);
        this.ne = requestFcp ? fcpLength : null;
    }

    // Note: Left out use case Select parent folder requesting File Control Parameter gemSpec_Cos#14.2.6.12

    // Note: Left out use case Select parent folder requesting File Control Parameter gemSpec_Cos#14.2.6.14

    private int calculateP2(final boolean requestFCP, final boolean nextOccurrence) {
        if (requestFCP) {
            p2 = RESPONSE_TYPE_FCP;
        } else {
            p2 = RESPONSE_TYPE_NO_RESPONSE;
        }
        if (nextOccurrence) {
            p2 += FILE_OCCURRENCE_NEXT;
        } else {
            p2 += FILE_OCCURRENCE_FIRST;
        }
        return p2;
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
