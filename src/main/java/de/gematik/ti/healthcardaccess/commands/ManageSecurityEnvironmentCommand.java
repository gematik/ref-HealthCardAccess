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

import cardfilesystem.egk21mf.Sk;
import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.GemCvCertificate;
import de.gematik.ti.healthcardaccess.cardobjects.Key;
import de.gematik.ti.healthcardaccess.cardobjects.PsoAlgorithm;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.EnumsValidationChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.IntegerRangeChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ValueStateChecker;
import de.gematik.ti.utils.primitives.Bytes;

/**
 * Commands representing Manage Security Environment command in gemSpec_COS#14.9.9
 */
public class ManageSecurityEnvironmentCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x22;

    private static final int MODE_SELECT_SE_IDENTIFIER = 0xF3;
    private static final int MODE_SET_INTERNAL_KEY_P1 = 0x41;
    private static final int MODE_SET_EXTERNAL_KEY_P1 = 0x81;
    private static final int MODE_SET_SECRET_KEY_OBJECT_P1 = 0xC1;
    private static final int MODE_SET_PRIVATE_KEY_P1 = 0x41;
    private static final int MODE_SET_PUBLIC_KEY_P1 = 0x81;

    private static final int MODE_AFFECTED_LIST_ELEMENT_IS_INT_AUTH_P2 = 0xA4;
    private static final int MODE_AFFECTED_LIST_ELEMENT_IS_EXT_AUTH_P2 = 0xA4;
    private static final int MODE_AFFECTED_LIST_ELEMENT_IS_SIGNATURE_CREATION = 0xB6;
    private static final int MODE_AFFECTED_LIST_ELEMENT_IS_VERIFY_CERTIFICATE = 0xB6;
    private static final int MODE_AFFECTED_LIST_ELEMENT_IS_DATA_DECIPHER = 0xB8;
    private static final int MODE_AFFECTED_LIST_ELEMENT_IS_DATA_ENCIPHER = 0xB8;

    private static final int SE_NUMBER_MIN = 1;
    private static final int SE_NUMBER_MAX = 4;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);// NOCS(LAC): Response Code
    }
    private final ISanityChecker<Enum> mseUsecasechecker = EnumsValidationChecker.getInstance();
    private final ISanityChecker<Integer> rangeChecker = IntegerRangeChecker.getInstance();
    private final ISanityChecker<Boolean> stateChecker = ValueStateChecker.getInstance();

    /**
     * Use Case Change Security Environment Identifier gemSpec_COS#14.9.9.1
     *
     * @param seNumber
     *            security Environment Number in [1,4]
     */
    public ManageSecurityEnvironmentCommand(final int seNumber) {
        super(CLA, INS);
        this.p1 = MODE_SELECT_SE_IDENTIFIER;
        this.p2 = seNumber;
        // gemSpec_COS#N007.900
        rangeChecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.seNumber.errMsg").setSpecialConfigurationPair("minValue", SE_NUMBER_MIN)
                .setSpecialConfigurationPair("maxValue", SE_NUMBER_MAX)
                .check(seNumber);
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

    // gemSpec_COS#14.9.9.2-14.9.9.12
    /**
     * List of different use cases when selecting a key while managing the security environment
     */
    public enum MseUseCase {
        KEY_SELECTION_FOR_INTERNAL_SYMMETRIC_AUTHENTICATION,
        KEY_SELECTION_FOR_INTERNAL_ASYMMETRIC_AUTHENTICATION,
        KEY_SELECTION_FOR_EXTERNAL_SYMMETRIC_AUTHENTICATION,
        KEY_SELECTION_FOR_EXTERNAL_ASYMMETRIC_AUTHENTICATION,
        KEY_SELECTION_FOR_SYMMETRIC_TWO_WAY_AUTHENTICATION,
        KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITHOUT_CURVES,
        KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITH_CURVES,
        KEY_SELECTION_FOR_SIGNING_KEY,
        KEY_SELECTION_FOR_CV_CERTIFICATE_VALIDATION,
        KEY_SELECTION_FOR_DATA_DE_OR_RECODING,
        KEY_SELECTION_FOR_DATA_ENCODING
    }

    /**
     * Use cases Key Selection for authentication and encryption gemSpec_COS#14.9.9.3 + 14.9.9.9 + 14.9.9.11
     *
     * @param mseUseCase
     * @param psoAlgorithm
     * @param key
     * @param dfSpecific
     */
    public ManageSecurityEnvironmentCommand(final MseUseCase mseUseCase, final PsoAlgorithm psoAlgorithm, final Key key,
            final boolean dfSpecific) throws IOException {
        super(CLA, INS);
        final MseUseCase[] validMse = {
                MseUseCase.KEY_SELECTION_FOR_INTERNAL_ASYMMETRIC_AUTHENTICATION,
                MseUseCase.KEY_SELECTION_FOR_SIGNING_KEY,
                MseUseCase.KEY_SELECTION_FOR_DATA_DE_OR_RECODING };
        mseUsecasechecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.Status.errMsg").setSpecifiedValues(validMse).check(mseUseCase);

        stateChecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.Key.errMsg").check(key != null);
        if (MseUseCase.KEY_SELECTION_FOR_INTERNAL_ASYMMETRIC_AUTHENTICATION == mseUseCase) { // gemSpec_COS#14.9.9.3
            this.p1 = MODE_SET_INTERNAL_KEY_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_INT_AUTH_P2;
            this.data = Bytes.concatNullables(// '8401||keyRef||8001||algId
                    new DERTaggedObject(false, 4, new DEROctetString(new byte[] { (byte) key.calculateKeyReference(dfSpecific) })).getEncoded(),
                    new DERTaggedObject(false, 0, new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() })).getEncoded());
        } else if (MseUseCase.KEY_SELECTION_FOR_SIGNING_KEY == mseUseCase) { // gemSpec_COS#14.9.9.9
            this.p1 = MODE_SET_PRIVATE_KEY_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_SIGNATURE_CREATION;
            this.data = Bytes.concatNullables(// '8401||keyRef||8001 algId'
                    new DERTaggedObject(false, 4, new DEROctetString(new byte[] { (byte) key.calculateKeyReference(dfSpecific) })).getEncoded(),
                    new DERTaggedObject(false, 0, new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() })).getEncoded());
        } else if (MseUseCase.KEY_SELECTION_FOR_DATA_DE_OR_RECODING == mseUseCase) { // gemSpec_COS#14.9.9.11
            this.p1 = MODE_SET_PRIVATE_KEY_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_DATA_DECIPHER;
            this.data = Bytes.concatNullables(// '8401||keyRef||8001 algId'
                    new DERTaggedObject(false, 4, new DEROctetString(new byte[] { (byte) key.calculateKeyReference(dfSpecific) })).getEncoded(),
                    new DERTaggedObject(false, 0, new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() })).getEncoded());
        }
    }

    /**
     * Use case Key Selection for symmetric card connection without curves gemSpec_COS#14.9.9.7
     *
     * @param mseUseCase
     * @param key
     * @param dfSpecific
     * @param oid
     * @throws IOException
     */
    public ManageSecurityEnvironmentCommand(final MseUseCase mseUseCase, final Key key, final boolean dfSpecific, final byte[] oid) throws IOException {
        super(CLA, INS);

        mseUsecasechecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.MseUseCase.errMsg")
                .setSpecifiedValues(MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITHOUT_CURVES)
                .check(mseUseCase);

        if (MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITHOUT_CURVES == mseUseCase) { // gemSpec_COS#14.9.9.7
            stateChecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.Key.errMsg").check(key != null);
            this.p1 = MODE_SET_SECRET_KEY_OBJECT_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_EXT_AUTH_P2;
            this.data = Bytes.concatNullables(// '80 I2OS(OctetLength(OID), 1) || OID || 83 01 || keyRef'
                    new DERTaggedObject(false, 0, new DEROctetString(oid)).getEncoded(),
                    new DERTaggedObject(false, 3, new DEROctetString(new byte[] { (byte) (byte) key.calculateKeyReference(dfSpecific) })).getEncoded());
        }
    }

    /**
     * Use case Key Selection for symmetric card connection with curves gemSpec_COS#14.9.9.8
     *
     * @param mseUseCase
     * @param key
     * @param dfSpecific
     * @param oid
     * @param idDomain
     * @throws IOException
     */
    public ManageSecurityEnvironmentCommand(final MseUseCase mseUseCase, final Key key, final boolean dfSpecific, final byte[] oid, final int idDomain)
            throws IOException {
        super(CLA, INS);

        mseUsecasechecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.MseUseCase.errMsg")
                .setSpecifiedValues(MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITH_CURVES)
                .check(mseUseCase);

        if (MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITH_CURVES == mseUseCase) { // gemSpec_COS#14.9.9.8
            stateChecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.Key.errMsg").check(key != null);
            // gemSpec_COS#N102.454
            stateChecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.idDomain.errMsg")
                    .check(idDomain == 0x0D || idDomain == 0x10 || idDomain == 0x11);

            this.p1 = MODE_SET_SECRET_KEY_OBJECT_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_EXT_AUTH_P2;
            this.data = Bytes.concatNullables(// '80 I2OS(OctetLength(OID), 1) || OID || 83 01 || keyRef' || 84 01 || idDomain
                    new DERTaggedObject(false, 0, new DEROctetString(oid)).getEncoded(),
                    new DERTaggedObject(false, 3, new DEROctetString(new byte[] { (byte) (byte) key.calculateKeyReference(dfSpecific) })).getEncoded(),
                    new DERTaggedObject(false, 4, new DEROctetString(new byte[] { (byte) idDomain })).getEncoded());
        }
    }

    /**
     * Use case Key Selection for checking Card Verifiable Certificates gemSpec_COS#14.9.9.10
     * 
     * @param mseUseCase
     * @param gemCvCertificate
     * @throws IOException
     */
    public ManageSecurityEnvironmentCommand(final MseUseCase mseUseCase, final GemCvCertificate gemCvCertificate)
            throws IOException {
        super(CLA, INS);

        mseUsecasechecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.MseUseCase.errMsg")
                .setSpecifiedValues(MseUseCase.KEY_SELECTION_FOR_CV_CERTIFICATE_VALIDATION)
                .check(mseUseCase);

        if (MseUseCase.KEY_SELECTION_FOR_CV_CERTIFICATE_VALIDATION == mseUseCase) { // gemSpec_COS#14.9.9.10
            this.p1 = MODE_SET_PUBLIC_KEY_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_VERIFY_CERTIFICATE;
            // '8308 ||keyRef'
            this.data = new DERTaggedObject(false, 3, new DEROctetString(gemCvCertificate.getCertificationAuthorityReferenceContents())).getEncoded();
        }
    }

    /**
     * Use cases Key Selection with key reference gemSpec_COS#14.9.9.5 + 14.9.9.12
     * 
     * @param mseUseCase
     * @param psoAlgorithm
     * @param keyReference
     */
    public ManageSecurityEnvironmentCommand(final MseUseCase mseUseCase, final PsoAlgorithm psoAlgorithm, final byte[] keyReference) throws IOException {
        super(CLA, INS);

        mseUsecasechecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.MseUseCase.errMsg")
                .setSpecifiedValues(MseUseCase.KEY_SELECTION_FOR_EXTERNAL_ASYMMETRIC_AUTHENTICATION, MseUseCase.KEY_SELECTION_FOR_DATA_ENCODING)
                .check(mseUseCase);

        if (MseUseCase.KEY_SELECTION_FOR_EXTERNAL_ASYMMETRIC_AUTHENTICATION == mseUseCase) { // gemSpec_COS#14.9.9.5
            this.p1 = MODE_SET_EXTERNAL_KEY_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_EXT_AUTH_P2;
            this.data = Bytes.concatNullables(// '830C||keyRef||8001 algId'
                    new DERTaggedObject(false, 3, new DEROctetString(keyReference)).getEncoded(),
                    new DERTaggedObject(false, 0, new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() })).getEncoded());
        } else if (MseUseCase.KEY_SELECTION_FOR_DATA_ENCODING == mseUseCase) { // gemSpec_COS#14.9.9.12
            this.p1 = MODE_SET_PUBLIC_KEY_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_DATA_ENCIPHER;
            this.data = Bytes.concatNullables(// '830C||keyRef||8001 algId'
                    new DERTaggedObject(false, 3, new DEROctetString(keyReference)).getEncoded(),
                    new DERTaggedObject(false, 0, new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() })).getEncoded());
        }
    }

    /**
     * Use cases Key Selection for symmetric authentication gemSpec_COS#14.9.9.2 + 14.9.9.4 + 14.9.9.6
     *
     * @param mseUseCase
     * @param psoAlgorithm
     * @param cmsSymkey
     * @throws IOException
     */
    public ManageSecurityEnvironmentCommand(final MseUseCase mseUseCase, final PsoAlgorithm psoAlgorithm,
            final Sk.ICMSSymkey cmsSymkey) throws IOException {
        super(CLA, INS);
        final MseUseCase[] validMse = { MseUseCase.KEY_SELECTION_FOR_INTERNAL_SYMMETRIC_AUTHENTICATION,
                MseUseCase.KEY_SELECTION_FOR_EXTERNAL_SYMMETRIC_AUTHENTICATION, MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_TWO_WAY_AUTHENTICATION
        };

        mseUsecasechecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.Status.errMsg").setSpecifiedValues(validMse).check(mseUseCase);

        stateChecker.setMsgIncaseError("ManageSecurityEnvironmentCommand.Key.errMsg").check(cmsSymkey != null);
        if (MseUseCase.KEY_SELECTION_FOR_INTERNAL_SYMMETRIC_AUTHENTICATION == mseUseCase) { // gemSpec_COS#14.9.9.2
            this.p1 = MODE_SET_INTERNAL_KEY_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_INT_AUTH_P2;
            this.data = Bytes.concatNullables(// '83-L_83-keyRef||8001||algId
                    new DERTaggedObject(false, 3, new DEROctetString(new byte[] { (byte) cmsSymkey.getKID() })).getEncoded(),
                    new DERTaggedObject(false, 0, new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() })).getEncoded());
        } else if (MseUseCase.KEY_SELECTION_FOR_EXTERNAL_SYMMETRIC_AUTHENTICATION == mseUseCase
                || MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_TWO_WAY_AUTHENTICATION == mseUseCase) { // gemSpec_COS#14.9.9.4
                                                                                                  // + 14.9.9.6
            this.p1 = MODE_SET_EXTERNAL_KEY_P1;
            this.p2 = MODE_AFFECTED_LIST_ELEMENT_IS_EXT_AUTH_P2;
            this.data = Bytes.concatNullables(// '83-L_83-keyRef||8001||algId
                    new DERTaggedObject(false, 3, new DEROctetString(new byte[] { (byte) cmsSymkey.getKID() })).getEncoded(),
                    new DERTaggedObject(false, 0, new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() })).getEncoded());
        }
    }

}
