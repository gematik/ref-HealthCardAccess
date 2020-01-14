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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.asn1.ASN1ApplicationSpecific;
import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERApplicationSpecific;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DEROutputStream;
import org.spongycastle.asn1.DERSequence;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.jce.spec.ECPublicKeySpec;
import org.spongycastle.math.ec.ECPoint;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.PsoAlgorithm;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.CmdDataChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.EnumsValidationChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;

/**
 * Commands representing Encipher command in gemSpec_COS#14.8.4
 */
public class PsoEncipher extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x2A;
    private static final int ANSWER_IS_CIPHER_P1 = 0x86;
    private static final int DATA_IS_PLAINTEXT_P2 = 0x80;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(PsoEncipher.class);
    ISanityChecker<byte[]> cmdDataChecker = CmdDataChecker.getInstance();
    ISanityChecker<Enum> checkerPsoAlgo = EnumsValidationChecker.getInstance();

    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6400, Response.ResponseStatus.KEY_INVALID);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A81, Response.ResponseStatus.UNSUPPORTED_FUNCTION);// NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);// NOCS(LAC): Response Code
    }

    /**
     * Use case Encipher using transmitted RSA key
     *
     * @param psoAlgorithm
     * @param publicKey
     * @param dataToBeEnciphered
     */
    public PsoEncipher(final PsoAlgorithm psoAlgorithm, final RSAPublicKey publicKey, final byte[] dataToBeEnciphered) {
        super(CLA, INS);
        this.p1 = ANSWER_IS_CIPHER_P1;
        this.p2 = DATA_IS_PLAINTEXT_P2;

        byte[] plainDo = computePlainDoRsaEncipher(psoAlgorithm, publicKey, dataToBeEnciphered);
        this.data = plainDo;
        this.ne = EXPECT_ALL_WILDCARD;

        cmdDataChecker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoEncipher.class)
                .setCurrentParameter(psoAlgorithm).check(data);

        checkerPsoAlgo.setSpecifiedValues(PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_ENCIPHER_PKCS1_V1_5, PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_ENCIPHER_OAEP)
                .setMsgIncaseError("PsoEncipher.RSA.errMsg")
                .check(psoAlgorithm.getAlgorithm());

    }

    /**
     * Use case Encipher using transmitted ELC key gemSpec_COS#14.8.4.2
     *
     * @param psoAlgorithm
     * @param publicKey
     * @param dataToBeEnciphered
     */
    public PsoEncipher(final PsoAlgorithm psoAlgorithm, final ECPublicKey publicKey, final byte[] dataToBeEnciphered) {
        super(CLA, INS);
        this.p1 = ANSWER_IS_CIPHER_P1;
        this.p2 = DATA_IS_PLAINTEXT_P2;

        byte[] plainDo = computePlainDoEllipticCurveEncipher(psoAlgorithm, publicKey, dataToBeEnciphered);
        this.data = plainDo;
        this.ne = EXPECT_ALL_WILDCARD;

        checkerPsoAlgo.setSpecifiedValues(PsoAlgorithm.Algorithm.DE_ENCRYPT_ELC_SHARED_SECRET_CALCULATION)
                .setMsgIncaseError("PsoEncipher.ELC.errMsg")
                .check(psoAlgorithm.getAlgorithm());
        cmdDataChecker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoEncipher.class)
                .setCurrentParameter(psoAlgorithm).check(data);

    }

    /**
     * Use Case 14.8.4.3: Encipher by stored RSA key + 14.8.4.4: Encipher by stored ELC key + 14.8.4.5: Encipher by symmetric key
     *
     * @param psoAlgorithm
     * @param dataToBeEnciphered
     */
    public PsoEncipher(final PsoAlgorithm psoAlgorithm, final byte[] dataToBeEnciphered) {
        super(CLA, INS);
        this.p1 = ANSWER_IS_CIPHER_P1;
        this.p2 = DATA_IS_PLAINTEXT_P2;
        this.data = dataToBeEnciphered;
        this.ne = EXPECT_ALL_WILDCARD;

        cmdDataChecker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoEncipher.class)
                .setCurrentParameter(psoAlgorithm).check(data);

        checkerPsoAlgo.setSpecifiedValues(PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_ENCIPHER_PKCS1_V1_5, PsoAlgorithm.Algorithm.DE_ENCRYPT_RSA_ENCIPHER_OAEP,
                PsoAlgorithm.Algorithm.DE_ENCRYPT_ELC_SHARED_SECRET_CALCULATION, PsoAlgorithm.Algorithm.DE_ENCRYPT_AES_SESSIONKEY,
                PsoAlgorithm.Algorithm.DE_ENCRYPT_DES_SESSIONKEY_OPTION_DES).setMsgIncaseError("PsoEncipher.errMsg")
                .check(psoAlgorithm.getAlgorithm());

    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

    private byte[] computePlainDoRsaEncipher(final PsoAlgorithm psoAlgorithm, final RSAPublicKey publicKey, final byte[] dataToBeEnciphered) {
        // gemSpec_COS#N091.700{b, c}
        // algDo
        ASN1OctetString alg = new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() });
        ASN1TaggedObject algDo = new DERTaggedObject(false, 0x00, alg);

        // keyDo
        byte[] pukNBytes = publicKey.getModulus().toByteArray();
        byte[] pukEBytes = publicKey.getPublicExponent().toByteArray();
        ASN1OctetString pukN = new DEROctetString(pukNBytes);
        ASN1OctetString pukE = new DEROctetString(pukEBytes);
        ASN1TaggedObject pukNDo = new DERTaggedObject(false, 0x01, pukN);
        ASN1TaggedObject pukEDo = new DERTaggedObject(false, 0x02, pukE);
        ASN1EncodableVector asn1KeyVector = new ASN1EncodableVector();
        asn1KeyVector.add(pukNDo);
        asn1KeyVector.add(pukEDo);
        ASN1ApplicationSpecific keyDo = new DERApplicationSpecific(0x49, asn1KeyVector);

        // mDo
        DERTaggedObject mDo = new DERTaggedObject(false, 0x00, new DEROctetString(dataToBeEnciphered));

        // plainDo
        ASN1EncodableVector plain = new ASN1EncodableVector();
        plain.add(algDo);
        plain.add(keyDo);
        plain.add(mDo);
        ASN1Sequence asn1seq2 = new DERSequence(plain);
        ASN1TaggedObject plainDo = new DERTaggedObject(false, 0x00, asn1seq2);

        return getResult(plainDo);
    }

    private byte[] computePlainDoEllipticCurveEncipher(final PsoAlgorithm psoAlgorithm, final ECPublicKey ecPublicKey, final byte[] dataToBeEnciphered) {
        // gemSpec_COS#N091.700d
        // algDo
        ASN1OctetString alg = new DEROctetString(new byte[] { (byte) psoAlgorithm.getIdentifier() });
        ASN1TaggedObject algDo = new DERTaggedObject(false, 0x00, alg);

        // extract oid from ECPublicKey
        byte[] publicKeyEncoded = ecPublicKey.getEncoded();
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(publicKeyEncoded));
        AlgorithmIdentifier algId = subjectPublicKeyInfo.getAlgorithm();
        ASN1Encodable oidDo = algId.getParameters();

        // keyDo
        // {PO}_{B} ... public point P_B of receiver
        // transform point from java.security.interfaces.ECPublicKey to org.spongycastle.jce.spec.ECPublicKeySpec
        ECParameterSpec ecParameterSpec = EC5Util.convertSpec(ecPublicKey.getParams(), false);
        ECPoint point = ecParameterSpec.getCurve().createPoint(ecPublicKey.getW().getAffineX(), ecPublicKey.getW().getAffineY());

        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecParameterSpec);
        byte[] uncompressedPubKey = pubSpec.getQ().getEncoded(false);

        ASN1OctetString poB = new DEROctetString(uncompressedPubKey);
        ASN1TaggedObject poBDo = new DERTaggedObject(false, 0x06, poB);
        ASN1ApplicationSpecific keyDo = null;
        try {
            keyDo = new DERApplicationSpecific(0x49, poBDo);
        } catch (IOException e) {
            LOG.error(e.toString(), e);
        }

        // mDo
        DERTaggedObject mDo = new DERTaggedObject(false, 0x00, new DEROctetString(dataToBeEnciphered));

        // plainDo
        ASN1EncodableVector plain = new ASN1EncodableVector();
        plain.add(algDo);
        plain.add(oidDo);
        plain.add(keyDo);
        plain.add(mDo);
        ASN1Sequence asn1seq2 = new DERSequence(plain);
        ASN1TaggedObject plainDo = new DERTaggedObject(false, 0x00, asn1seq2);

        return getResult(plainDo);
    }

    private byte[] getResult(final ASN1TaggedObject plainDo) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] result = null;
        DEROutputStream dos = new DEROutputStream(bos);
        try {
            dos.writeObject(plainDo);
        } catch (IOException e) {
            LOG.error(e.toString(), e);
        } finally {
            result = bos.toByteArray();
            try {
                bos.close();
                dos.close();
            } catch (IOException e) {
                LOG.error("Error on close because " + e.toString());
            }
        }
        return result;
    }
}
