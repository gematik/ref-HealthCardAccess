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

package cardfilesystem.smcb21mf.df.esign;

public class Ef {

    public static final ChciOsigR2048 C_HCI_OSIG_R2048 = new ChciOsigR2048();
    public static final ChciAutR2048 C_HCI_AUT_R2048 = new ChciAutR2048();
    public static final ChciEncR2048 C_HCI_ENC_R2048 = new ChciEncR2048();
    public static final ChciOsigE256 C_HCI_OSIG_E256 = new ChciOsigE256();
    public static final ChciAutE256 C_HCI_AUT_E256 = new ChciAutE256();
    public static final ChciEncE256 C_HCI_ENC_E256 = new ChciEncE256();

    public static class ChciOsigR2048 {
        public static final int FID = 0xC000;
        public static final int SFID = 0x10;
    }

    public static class ChciAutR2048 {
        public static final int FID = 0xC500;
        public static final int SFID = 0x01;
    }

    public static class ChciEncR2048 {
        public static final int FID = 0xC200;
        public static final int SFID = 0x02;
    }

    public static class ChciOsigE256 {
        public static final int FID = 0xC007;
        public static final int SFID = 0x07;
    }

    public static class ChciAutE256 {
        public static final int FID = 0xC506;
        public static final int SFID = 0x06;
    }

    public static class ChciEncE256 {
        public static final int FID = 0xC205;
        public static final int SFID = 0x05;
    }
}
