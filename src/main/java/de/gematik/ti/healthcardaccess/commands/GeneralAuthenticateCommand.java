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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.DERApplicationSpecific;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERTaggedObject;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.utils.primitives.Bytes;

/**
 * Commands representing the General Authenticate commands in gemSpec_COS#14.7.2
 */
public class GeneralAuthenticateCommand extends AbstractHealthCardCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralAuthenticateCommand.class);

    private static final int CLA_COMMAND_CHAINING = 0x10;
    private static final int CLA_NO_COMMAND_CHAINING = 0x00;
    private static final int INS = 0x86;
    private static final int NO_MEANING = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();

    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);
        RESPONSE_MESSAGES.put(0x6300, Response.ResponseStatus.AUTHENTICATION_FAILURE);
        RESPONSE_MESSAGES.put(0x6400, Response.ResponseStatus.PARAMETER_MISMATCH);
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);
        RESPONSE_MESSAGES.put(0x6983, Response.ResponseStatus.KEY_EXPIRED);
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_KEY_REFERENCE);
        RESPONSE_MESSAGES.put(0x6A80, Response.ResponseStatus.NUMBER_PRECONDITION_WRONG);
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);
    }

    /**
     * UseCase: gemSpec_COS#14.7.2.1.1 PACE for end-user cards, Step 1 a
     *
     * @param commandChaining
     *          -true for command chaining false if not
     * @throws IOException if an error occurred
     */
    public GeneralAuthenticateCommand(final boolean commandChaining) throws IOException {
        super(commandChaining ? CLA_COMMAND_CHAINING : CLA_NO_COMMAND_CHAINING, INS);
        this.ne = NE_MAX_SHORT_LENGTH;
        this.p1 = NO_MEANING;
        this.p2 = NO_MEANING;

        DERApplicationSpecific app = new DERApplicationSpecific(28, new ASN1EncodableVector());
        this.data = app.getEncoded();
    }

    /**
     * UseCase: gemSpec_COS#14.7.2.1.1 PACE for end-user cards, Step 2a (tagNo 1), 3a (3) , 5a (5)
     *
     * @param commandChaining
     *          -true for command chaining false if not
     * @param data byteArray with data
     * @throws IOException if an error occurred
     */
    public GeneralAuthenticateCommand(final boolean commandChaining, final byte[] data, final int tagNo) throws IOException {
        super(commandChaining ? CLA_COMMAND_CHAINING : CLA_NO_COMMAND_CHAINING, INS);
        this.ne = NE_MAX_SHORT_LENGTH;
        this.p1 = NO_MEANING;
        this.p2 = NO_MEANING;

        this.data = Bytes.concatNullables(//
                new DERApplicationSpecific(28, new DERTaggedObject(false, tagNo, new DEROctetString(data))).getEncoded());
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
