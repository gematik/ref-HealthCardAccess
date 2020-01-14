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

import java.nio.ByteBuffer;
import java.util.Objects;

import de.gematik.ti.utils.codec.Hex;

/**
 * A file identifier may reference any file. It consists of two bytes. The value '3F00'
 * is reserved for referencing the MF. The value 'FFFF' is reserved for future use. The value '3FFF' is reserved
 * (see below and 7.4.1). The value '0000' is reserved (see 7.2.2 and 7.4.1). In order to unambiguously select
 * any file by its identifier, all EFs and DFs immediately under a given DF shall have different file identifiers.
 * @see "ISO/IEC 7816-4"
 *
 */
public class FileIdentifier implements ICardObjectIdentifier {
    private final int fid;


    public FileIdentifier(final byte[] fid) {
        Objects.requireNonNull(fid);
        if (fid.length != 2) {
            throw new IllegalArgumentException("requested length of byte array for a File Identifier value is 2 but was " + fid.length);
        }
        ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);

        for (int i = 0; i < fid.length; i++) {
            b.put(fid.length + i, fid[i]);
        }
        this.fid = b.getInt();

        sanityCheck();
    }

    public FileIdentifier(final int fid) {
        this.fid = fid;
        sanityCheck();
    }

    public FileIdentifier(final String hexFid) {
        this(Hex.decode(hexFid));
    }

    public byte[] getFid() {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        return buffer.putShort((short) fid).array();
    }

    private void sanityCheck() {
        // gemSpec_COS#N006.700, N006.900
        if (((fid < 0x1000 || fid > 0xFEFF) && fid != 0x011C) || fid == 0x3FFF) {
            throw new IllegalArgumentException("File Identifier is out of range: 0x" + Hex.encodeHexString(getFid(), false));
        }
    }
}
