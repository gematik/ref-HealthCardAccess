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

package de.gematik.ti.healthcardaccess.cardobjects;

import java.io.IOException;

import org.spongycastle.asn1.ASN1ApplicationSpecific;
import org.spongycastle.asn1.ASN1EncodableVector;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1Object;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.DERApplicationSpecific;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.DERTaggedObject;
import org.spongycastle.asn1.eac.CertificateBody;
import org.spongycastle.asn1.eac.CertificateHolderAuthorization;
import org.spongycastle.asn1.eac.EACTags;
import org.spongycastle.asn1.eac.ECDSAPublicKey;
import org.spongycastle.asn1.eac.PublicKeyDataObject;

import de.gematik.ti.healthcardaccess.ICardItem;

/**
 * Card Verifiable Certificates (CVC) are digital certificates that are designed to be processed by devices with limited computing power such as smart cards.
 * This is achieved by using simple TLV (Tag Length Value) encoding with fixed fields. Fixed fields means that each field in the certificate is of fixed,
 * or maximum, length and each field comes in a well defined order. This makes parsing easy, in contrast to asn.1 parsing which requires more
 * processing and has to keep fields in memory while parsing nested content.
 *
 * @see "https://en.wikipedia.org/wiki/Card_Verifiable_Certificate"
 *
 * @created 05-Jul-2018 14:33:51
 */
public class GemCvCertificate extends ASN1Object implements ICardItem {

    private static final int BODY_VALID = 0x01;
    private static final int SIGN_VALID = 0x02;
    // TODO extract EACTags from spongy castle library into this class
    private byte[] certificateContent;
    private CertificateBody certificateBody;
    private byte[] signature;
    private int valid;

    private DERApplicationSpecific certificateProfileIdentifier; // version of the certificate format. Must be 0 (version 1)
    private DERApplicationSpecific certificationAuthorityReference; // uniquely identifies the issuinng CA's signature key pair
    private PublicKeyDataObject publicKey; // stores the encoded public key
    private DERApplicationSpecific certificateHolderReference; // associates the public key contained in the certificate with a unique name
    private CertificateHolderAuthorization certificateHolderAuthorization; // Encodes the role of the holder (i.e. CVCA, DV, IS) and assigns read/write access
                                                                           // rights to data groups storing sensitive data
    private DERApplicationSpecific certificateEffectiveDate; // the date of the certificate generation
    private DERApplicationSpecific certificateExpirationDate; // the date after wich the certificate expires
    private int certificateType = 0; // bit field of initialized data. This will tell us if the data are valid.

    /**
     * Create an iso7816Certificate (gematik specific) structure from a input data byte array.
     * 
     * @param certificateData
     * @throws IOException
     */
    public GemCvCertificate(final byte[] certificateData) throws IOException {
        initFrom(new ASN1InputStream(certificateData));
    }

    /**
     * Create an iso7816Certificate (gematik specific) structure from an ASN1InputStream.
     *
     * @param aIS
     *            the byte stream to parse.
     * @throws IOException
     *             if there is a problem parsing the data.
     */
    public GemCvCertificate(final ASN1InputStream aIS) throws IOException {
        initFrom(aIS);
    }

    private void initFrom(final ASN1InputStream aIS) throws IOException {
        ASN1Primitive obj;
        while ((obj = aIS.readObject()) != null) {
            if (obj instanceof DERApplicationSpecific) {
                setPrivateData((DERApplicationSpecific) obj);
            } else {
                throw new IOException("HealthCardStatusInvalid Input Stream for creating an Iso7816CertificateStructure");
            }
        }
    }

    private void setPrivateCertificateBodyData(final DERApplicationSpecific appSpe) throws IOException {
        byte[] content;
        if (appSpe.getApplicationTag() == EACTags.CERTIFICATE_CONTENT_TEMPLATE) {
            content = appSpe.getContents();
        } else {
            throw new IOException("Bad tag : not an iso7816 CERTIFICATE_CONTENT_TEMPLATE");
        }
        try (ASN1InputStream aIS = new ASN1InputStream(content)) {
            ASN1Primitive obj;
            while ((obj = aIS.readObject()) != null) {
                DERApplicationSpecific aSpe;

                if (obj instanceof DERApplicationSpecific) {
                    aSpe = (DERApplicationSpecific) obj;
                } else {
                    throw new IOException("Not a valid iso7816 content : not a DERApplicationSpecific Object :" + EACTags.encodeTag(appSpe) + obj.getClass());
                }
                switch (aSpe.getApplicationTag()) {
                    case EACTags.INTERCHANGE_PROFILE:
                        certificateProfileIdentifier = aSpe;
                        break;
                    case EACTags.ISSUER_IDENTIFICATION_NUMBER:
                        certificationAuthorityReference = aSpe;
                        break;
                    case EACTags.CARDHOLDER_PUBLIC_KEY_TEMPLATE:
                        publicKey = buildECDSAPublicKeyFromPublicKeyDo(aSpe);
                        break;
                    case EACTags.CARDHOLDER_NAME:
                        certificateHolderReference = aSpe;
                        break;
                    case EACTags.CERTIFICATE_HOLDER_AUTHORIZATION_TEMPLATE:
                        certificateHolderAuthorization = new CertificateHolderAuthorization(aSpe);
                        break;
                    case EACTags.APPLICATION_EFFECTIVE_DATE:
                        certificateEffectiveDate = aSpe;
                        break;
                    case EACTags.APPLICATION_EXPIRATION_DATE:
                        certificateExpirationDate = aSpe;
                        break;
                    default:
                        certificateType = 0;
                        throw new IOException("Not a valid iso7816 DERApplicationSpecific tag " + aSpe.getApplicationTag());
                }
            }
            // TODO this.certificateBody = buildCertificateBodyFromPrivateBodyData();
        }
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();

        if (null != certificateBody) {
            v.add(certificateBody);
        }

        try {
            v.add(new DERApplicationSpecific(false, EACTags.STATIC_INTERNAL_AUTHENTIFICATION_ONE_STEP, new DEROctetString(signature)));
        } catch (IOException e) {
            throw new IllegalStateException("unable to convert signature!");
        }

        return new DERApplicationSpecific(EACTags.CARDHOLDER_CERTIFICATE, v);
    }

    private ECDSAPublicKey buildECDSAPublicKeyFromPublicKeyDo(final DERApplicationSpecific aSpe) throws IOException {
        try (ASN1InputStream pub = new ASN1InputStream(aSpe.getContents())) {
            ASN1Primitive obj;
            ASN1ObjectIdentifier oid;
            DERTaggedObject pubKeyTagged = null;
            ASN1OctetString pubKeyOctet = null;
            byte[] pubKeyBytes = null;
            obj = pub.readObject();
            if (obj instanceof ASN1ObjectIdentifier) {
                oid = (ASN1ObjectIdentifier) obj;
            } else {
                throw new IllegalArgumentException("no Oid in PublicKey");
            }
            obj = pub.readObject();
            if (obj instanceof DERTaggedObject) {
                pubKeyTagged = (DERTaggedObject) obj;
                pubKeyOctet = (DEROctetString) pubKeyTagged.getObject();
                pubKeyBytes = pubKeyOctet.getOctets();
            }
            return new ECDSAPublicKey(oid, pubKeyBytes);
        }
    }

    public byte[] getCertificationAuthorityReferenceContents() {
        return certificationAuthorityReference.getContents();
    }

    public byte[] getCertificateContent() {
        return this.certificateContent;
    }

    public DERApplicationSpecific getHolderReference() throws IOException {
        return certificateHolderReference;
    }

    private void setPrivateData(final ASN1ApplicationSpecific appSpe) throws IOException {
        valid = 0;
        if (appSpe.getApplicationTag() == EACTags.CARDHOLDER_CERTIFICATE) {
            this.certificateContent = appSpe.getEncoded();
            try (ASN1InputStream content = new ASN1InputStream(appSpe.getContents())) {
                ASN1Primitive tmpObj;
                while ((tmpObj = content.readObject()) != null) {
                    DERApplicationSpecific aSpe;
                    if (tmpObj instanceof DERApplicationSpecific) {
                        aSpe = (DERApplicationSpecific) tmpObj;
                        switch (aSpe.getApplicationTag()) {
                            case EACTags.CERTIFICATE_CONTENT_TEMPLATE:
                                setPrivateCertificateBodyData(aSpe);
                                valid |= BODY_VALID;
                                break;
                            case EACTags.STATIC_INTERNAL_AUTHENTIFICATION_ONE_STEP:
                                signature = aSpe.getContents();
                                valid |= SIGN_VALID;
                                break;
                            default:
                                throw new IOException("HealthCardStatusInvalid tag, not an Iso7816CertificateStructure :" + aSpe.getApplicationTag());
                        }
                    } else {
                        throw new IOException("HealthCardStatusInvalid Object, not an Iso7816CertificateStructure");
                    }
                }
            }
        } else {
            throw new IOException("not a CARDHOLDER_CERTIFICATE :" + appSpe.getApplicationTag());
        }

        if (valid != (SIGN_VALID | BODY_VALID)) {
            throw new IOException("invalid CARDHOLDER_CERTIFICATE :" + appSpe.getApplicationTag());
        }
    }
}
