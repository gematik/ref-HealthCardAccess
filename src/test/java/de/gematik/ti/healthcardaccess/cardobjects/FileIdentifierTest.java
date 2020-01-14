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

import org.junit.Assert;
import org.junit.Test;

public class FileIdentifierTest {

    private static final String FID_HEX_STRING = "011C";
    private static final int FID_INTEGER = 0x011C;

    @Test
    public void shouldConstructEqualObjects() {
        FileIdentifier fi1 = new FileIdentifier(FID_INTEGER);
        FileIdentifier fi2 = new FileIdentifier(FID_HEX_STRING);
        Assert.assertArrayEquals(fi1.getFid(), fi2.getFid());
    }

    @Test
    public void shouldConvertToEqualObject() {
        FileIdentifier fi1 = new FileIdentifier(FID_HEX_STRING);
        FileIdentifier fi2 = new FileIdentifier(fi1.getFid());
        Assert.assertArrayEquals(fi1.getFid(), fi2.getFid());
    }
}
