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

package de.gematik.ti.healthcardaccess.entities;

import de.gematik.ti.utils.codec.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class Version2Test {

    @Test
    public void fromArray() throws IOException {
        Version2 version2 = Version2.fromArray(Hex.decode("EF2BC003020000C103040302C21045474B47322020202020202020010304C403010000C503020000C703010000"));
        Assert.assertEquals("020000", Hex.encodeHexString(version2.getFillingInstructionsEfAtrVersion())); // C5
        Assert.assertEquals("", Hex.encodeHexString(version2.getFillingInstructionsEfEnvironmentSettingsVersion())); //C3
        Assert.assertEquals("010000", Hex.encodeHexString(version2.getFillingInstructionsEfGdoVersion())); //C4
        Assert.assertEquals("", Hex.encodeHexString(version2.getFillingInstructionsEfKeyInfoVersion())); //C6
        Assert.assertEquals("010000", Hex.encodeHexString(version2.getFillingInstructionsEfLoggingVersion())); //C7
        Assert.assertEquals("020000", Hex.encodeHexString(version2.getFillingInstructionsVersion())); //C0
        Assert.assertEquals("040302", Hex.encodeHexString(version2.getObjectSystemVersion())); // C1
        Assert.assertEquals("45474B47322020202020202020010304", Hex.encodeHexString(version2.getProductIdentificationObjectSystemVersion()));  // C2
    }
}
