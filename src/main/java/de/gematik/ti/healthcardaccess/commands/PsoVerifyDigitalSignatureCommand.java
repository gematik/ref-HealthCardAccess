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

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.CmdDataChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.asn1.ASN1ApplicationSpecific;
import org.spongycastle.asn1.ASN1Encodable;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.DERApplicationSpecific;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.jce.spec.ECPublicKeySpec;
import org.spongycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.interfaces.ECPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Commands representing Verify Digital Signature command in gemSpec_COS#14.8.9
 */
public class PsoVerifyDigitalSignatureCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x2A;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(PsoVerifyDigitalSignatureCommand.class);

    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A80, Response.ResponseStatus.WRONG_CIPHER_TEXT);//NOCS(LAC): Response Code
    }

    /**
     * Use case verify an ELC signature gemSpec_COS#1.8.9.1
     *
     * @param ecPublicKey a ECPublicKey
     * @param hash the signed hash value
     * @param signature (normalized) signature hash to verify
     */
    public PsoVerifyDigitalSignatureCommand(final ECPublicKey ecPublicKey, final byte[] hash, final byte[] signature) {
        super(CLA, INS);
        p1 = 0x00;
        p2 = 0xA8;

        data = computeSignatureTemplateDo(ecPublicKey, hash, signature);
        ISanityChecker<byte[]> checker = CmdDataChecker.getInstance();
        checker.setMsgIncaseError("CmdDataInvalidStructure.errMsg").setCurrentParameter(PsoVerifyDigitalSignatureCommand.class)
                .check(data);
    }

    private byte[] computeSignatureTemplateDo(final ECPublicKey ecPublicKey, final byte[] hash, final byte[] signature) {
        // gemSpec_COS#N096.394

        // extract oid from ECPublicKey
        byte[] publicKeyEncoded = ecPublicKey.getEncoded();
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(publicKeyEncoded));
        AlgorithmIdentifier algId = subjectPublicKeyInfo.getAlgorithm();
        ASN1Encodable oidDo = algId.getParameters();

        // hash
        ASN1TaggedObject hashDo = new DERTaggedObject(false, 0x10, new DEROctetString(hash));

        // keyDo
        // {PO}_{B} ... public point P_B of signer
        // transform point from java.security.interfaces.ECPublicKey to org.spongycastle.jce.spec.ECPublicKeySpec
        ECParameterSpec ecParameterSpec = EC5Util.convertSpec(ecPublicKey.getParams(), false);
        ECPoint point = ecParameterSpec.getCurve().createPoint(ecPublicKey.getW().getAffineX(), ecPublicKey.getW().getAffineY());

        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecParameterSpec);
        byte[] uncompressedPubKey = pubSpec.getQ().getEncoded(false);

        ASN1OctetString poB = new DEROctetString(uncompressedPubKey);
        ASN1TaggedObject poBDo = new DERTaggedObject(false, 0x06, poB);
        ASN1ApplicationSpecific keyDo = null;
        ASN1TaggedObject taggedKeyDo = null;
        try {
            keyDo = new DERApplicationSpecific(0x49, poBDo);
            taggedKeyDo = new DERTaggedObject(false, 0x1C, new DEROctetString(keyDo));

        } catch (IOException e) {
            LOG.error("Error creating DER Objects because " + e, e);
        }

        // signature
        ASN1TaggedObject signatureDo = new DERTaggedObject(false, 0x1E, new DEROctetString(signature));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] result = null;
        try {
            bos.write(oidDo.toASN1Primitive().getEncoded());
            bos.write(hashDo.getEncoded());
            if (taggedKeyDo != null) {
                bos.write(taggedKeyDo.getEncoded());
            } else {
                throw new IOException("taggedKeyDo is null");
            }
            bos.write(signatureDo.getEncoded());
        } catch (IOException e) {
            LOG.error("Error writing ASN1Encodable to ByteArrayOutputStream.", e);
        } finally {
            try {
                result = bos.toByteArray();
                bos.close();
            } catch (IOException e) {
                LOG.error("Error on close() because " + e.toString());
            }
        }
        return result;
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }

}
