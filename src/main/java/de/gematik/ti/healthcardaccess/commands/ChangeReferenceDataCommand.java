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
import de.gematik.ti.healthcardaccess.cardobjects.Format2Pin;
import de.gematik.ti.healthcardaccess.cardobjects.Password;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.utils.primitives.Bytes;

/**
 * Commands representing Change/Set Reference Data Command gemSpec_COS#14.6.1
 */
public class ChangeReferenceDataCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x24;

    private static final int MODE_DATA_CONTAINS_OLD_AND_NEW_SECRET = 0x00;
    private static final int MODE_DATA_CONTAINS_NEW_SECRET = 0x01;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.WRONG_SECRET_WARNING_COUNT_00);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.WRONG_SECRET_WARNING_COUNT_01);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.WRONG_SECRET_WARNING_COUNT_02);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.WRONG_SECRET_WARNING_COUNT_03);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6581, Response.ResponseStatus.MEMORY_FAILURE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6983, Response.ResponseStatus.PASSWORD_BLOCKED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.WRONG_PASSWORD_LENGTH);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6988, Response.ResponseStatus.PASSWORD_NOT_FOUND);//NOCS(LAC): Response Code
    }

    /**
     * Use case Change Password Secret (Pin) gemSpec_COS#14.6.1.1
     *
     * @param password
     * @param dfSpecific
     * @param oldSecret
     * @param newSecret
     */
    public ChangeReferenceDataCommand(final Password password, final boolean dfSpecific, final Format2Pin oldSecret, final Format2Pin newSecret) { //NOCS(LAC): Command creation
        super(CLA, INS);
        this.p1 = MODE_DATA_CONTAINS_OLD_AND_NEW_SECRET;
        this.p2 = password.calculateKeyReference(dfSpecific);
        this.data = Bytes.concatNullables(oldSecret.getFormat2Pin(), newSecret.getFormat2Pin());
    }

    /**
     * Use case Set Password Secret (Pin) gemSpec_COS#14.6.1.2
     *
     * @param password
     * @param dfSpecific
     * @param newSecret
     */
    public ChangeReferenceDataCommand(final Password password, final boolean dfSpecific, final Format2Pin newSecret) {
        super(CLA, INS);
        this.p1 = MODE_DATA_CONTAINS_NEW_SECRET;
        this.p2 = password.calculateKeyReference(dfSpecific);
        this.data = newSecret.getFormat2Pin();
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
