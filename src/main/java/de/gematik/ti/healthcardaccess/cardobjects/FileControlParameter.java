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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.ASN1Set;
import org.spongycastle.asn1.ASN1TaggedObject;
import org.spongycastle.asn1.BERTags;
import org.spongycastle.asn1.DERApplicationSpecific;

import de.gematik.ti.healthcardaccess.ICardItem;
import de.gematik.ti.healthcardaccess.WrongCardDataException;
import de.gematik.ti.utils.codec.Hex;

/**
 * The File Control Parameter is a set of control parameters, i.e., logical, structural and security attributes as
 * listed in Table 12 from ISO/IEC 7816-4. Within the FCP, the context-specific class (first byte
 * from '80' to 'BF') is reserved for file control parameters; tags '85' and 'A5' reference discretionary data.
 * @see "ISO/IEC 7816-4"
 *
 */
public class FileControlParameter implements ICardItem {
    public static final int LCS_CREATION_STATE = 1;
    public static final int LCS_INITIALISATION_STATE = 3;
    public static final int LCS_OPERATIONAL_STATE_ACTIVATED = 5;
    public static final int LCS_OPERATIONAL_STATE_DEACTIVATED = 4;
    public static final int LCS_TERMINATION_STATE = 12;

    public enum LifeCycleStates {
        LCS_CREATION_STATE(1),
        LCS_INITIALISATION_STATE(3),
        LCS_OPERATIONAL_STATE_ACTIVATED(0x05, 0x07),
        LCS_OPERATIONAL_STATE_DEACTIVATED(0x04, 0x06),
        LCS_TERMINATION_STATE(0x0c, 0x0d, 0x0e, 0x0f);

        private static final Map<Integer, LifeCycleStates> MAP = new HashMap<>();
        private final int[] value;

        LifeCycleStates(final int... value) {
            this.value = value;
        }

        public int getValue() {
            return value[0];
        }

        static {
            for (LifeCycleStates lcs : LifeCycleStates.values()) {
                for (int v : lcs.value) {
                    MAP.put(v, lcs);
                }
            }
        }

        public static LifeCycleStates valueOf(final int value) {
            return MAP.get(value);
        }
    }

    public static final int TAG_FCP = 0x02; // gemSpec_COS#N013.900 Application specific and constructed content with tag 2
    public static final int TAG_NUMBER_OF_OCTET = 0x00; // gemSpec_COS#N014.000
    public static final int TAG_FILE_DESCRIPTOR = 0x02; // gemSpec_COS#N014.100
    public static final int TAG_FILE_IDENTIFIER = 0x03; // gemSpec_COS#N014.200
    public static final int TAG_APPLICATION_IDENTIFIER = 0x04; // gemSpec_COS#N014.300
    public static final int TAG_SHORT_FILE_IDENTIFIER = 0x08; // gemSpec_COS#N014.400
    public static final int TAG_LIFE_CYCLE_STATUS = 0x0A; // gemSpec_COS#N014.500

    private LifeCycleStates lifeCycleStatus = null;
    private int numberOfOctet = 0x00;
    private byte[] fileDescriptor = null;
    private byte[] fileIdentifier = null;
    private byte[] applicationIdentifier = null;
    private int shortFileIdentifier = 0x00;

    public FileControlParameter(final byte[] responseData) throws WrongCardDataException {
        if (responseData == null || responseData.length == 0) {
            throw new IllegalArgumentException("Non-Null value expected to create FileControlParameter from: " + Arrays.toString(responseData));
        }

        ASN1InputStream ais = new ASN1InputStream(new ByteArrayInputStream(responseData));
        try {
            DERApplicationSpecific derApplicationSpecific = (DERApplicationSpecific) ais.readObject();
            if (derApplicationSpecific.getApplicationTag() != TAG_FCP) {
                throw new WrongCardDataException(
                        "Try to convert response data to FCP, but wrong tag found: " + derApplicationSpecific.getApplicationTag() + ", instead of " + TAG_FCP);
            }

            ASN1Set asn1Set = ASN1Set.getInstance(derApplicationSpecific.getObject(BERTags.SET));
            Enumeration setEnum = asn1Set.getObjects();

            while (setEnum.hasMoreElements()) {
                Object object = setEnum.nextElement();
                ASN1TaggedObject asn1TaggedObject = null;

                if (object instanceof ASN1TaggedObject) {
                    asn1TaggedObject = (ASN1TaggedObject) object;
                }
                ASN1OctetString asn1OctetString = catchOctetStringFromTaggedObject(asn1TaggedObject);

                fillFcpElements(asn1TaggedObject, asn1OctetString);
            }
        } catch (IOException e) {
            throw new WrongCardDataException("IO exception when trying to read fcp from byte array", e);
        }
    }

    private void fillFcpElements(final ASN1TaggedObject asn1TaggedObject, final ASN1OctetString asn1OctetString) {
        if (asn1TaggedObject != null && asn1OctetString != null) {
            switch (asn1TaggedObject.getTagNo()) {
                case TAG_NUMBER_OF_OCTET:
                    numberOfOctet = new BigInteger(asn1OctetString.getOctets()).intValue();
                    break;
                case TAG_FILE_DESCRIPTOR:
                    fileDescriptor = asn1OctetString.getOctets();
                    break;
                case TAG_FILE_IDENTIFIER:
                    fileIdentifier = asn1OctetString.getOctets();
                    break;
                case TAG_APPLICATION_IDENTIFIER:
                    applicationIdentifier = asn1OctetString.getOctets();
                    break;
                case TAG_SHORT_FILE_IDENTIFIER:
                    shortFileIdentifier = new BigInteger(asn1OctetString.getOctets()).intValue();
                case TAG_LIFE_CYCLE_STATUS:
                    lifeCycleStatus = LifeCycleStates.valueOf((int) asn1OctetString.getOctets()[0]);
                    break;
                default:
                    break;
            }
        }
    }

    public final byte[] getFileDescriptor() {
        return fileDescriptor;
    }

    public final byte[] getFileIdentifier() {
        return fileIdentifier;
    }

    public final byte[] getApplicationIdentifier() {
        return applicationIdentifier;
    }

    public LifeCycleStates getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    public int getNumberOfOctet() {
        return numberOfOctet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("numberOfOctet: %d", numberOfOctet));
        sb.append(", fileDescriptor: ");
        sb.append(Hex.encodeHexString(fileDescriptor, false));
        sb.append(", lifeCycleState: ");
        sb.append(lifeCycleStatus);
        sb.append(", fid: ");
        sb.append(Hex.encodeHexString(fileIdentifier, false));
        sb.append(String.format(", sfid: %02x", shortFileIdentifier));
        sb.append(", aid: ");
        sb.append(Hex.encodeHexString(applicationIdentifier, false));
        return sb.toString().trim();
    }

    private ASN1OctetString catchOctetStringFromTaggedObject(final ASN1TaggedObject asn1TaggedObject) {
        ASN1OctetString asn1OctetString = null;

        if (asn1TaggedObject != null) {
            ASN1Primitive asn1Primitive = asn1TaggedObject.getObject();
            if (asn1Primitive instanceof ASN1OctetString) {
                asn1OctetString = (ASN1OctetString) asn1Primitive;
            }
        }

        return asn1OctetString;
    }
}
