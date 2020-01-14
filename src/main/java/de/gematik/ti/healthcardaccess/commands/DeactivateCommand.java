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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERTaggedObject;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.Key;
import de.gematik.ti.healthcardaccess.cardobjects.Password;
import de.gematik.ti.healthcardaccess.result.Response;

/**
 * Commands representing Deactivate Command gemSpec_COS#14.2.3
 */
public class DeactivateCommand extends AbstractHealthCardCommand {

    private static final int CLA = 0x00;
    private static final int INS = 0x04;
    private static final int MODE_CURRENT_SELECTION = 0x00;
    private static final int MODE_PASSWORD_REFERENCE = 0x10;
    private static final int MODE_SYM_OR_PRIVATE_KEY_REFERENCE = 0x20;
    private static final int MODE_PUBLIC_KEY_REFERENCE = 0x21;
    private static final int NO_MEANING = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_00);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_01);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_02);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_03);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6400, Response.ResponseStatus.OBJECT_TERMINATED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6581, Response.ResponseStatus.MEMORY_FAILURE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.VOLATILE_KEY_WITHOUT_LCS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);//NOCS(LAC): Response Code
    }

    public static final int TAG_NO = 3;

    /**
     * Use case Deactivate current EF gemSpec_Cos#14.2.3.1
     */
    public DeactivateCommand() {
        super(CLA, INS);
        this.p1 = MODE_CURRENT_SELECTION;
        this.p2 = NO_MEANING;
    }

    /**
     * Use case Deactivate private or symmetric key object gemSpec_Cos#14.2.3.2
     * 
     * @param key
     * @param dfSpecific
     */
    public DeactivateCommand(final Key key, final boolean dfSpecific) {
        super(CLA, INS);
        this.p1 = MODE_SYM_OR_PRIVATE_KEY_REFERENCE;
        this.p2 = key.calculateKeyReference(dfSpecific);
    }

    /**
     * Use case Deactivate public key object gemSpec_Cos#14.2.3.3
     * 
     * @param reference
     */
    public DeactivateCommand(final byte[] reference) throws IOException {
        super(CLA, INS);
        this.p1 = MODE_PUBLIC_KEY_REFERENCE;
        this.p2 = 0x00;
        // data: ´83 – I2OS(OctetLength(reference), 1) – reference´
        this.data = new DERTaggedObject(false, TAG_NO, new DEROctetString(reference)).getEncoded();
    }

    /**
     * Use case Deactivate password object gemSpec_Cos#14.2.3.4
     * 
     * @param password
     * @param dfSpecific
     */
    public DeactivateCommand(final Password password, final boolean dfSpecific) {
        super(CLA, INS);
        this.p1 = MODE_PASSWORD_REFERENCE;
        this.p2 = password.calculateKeyReference(dfSpecific);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
