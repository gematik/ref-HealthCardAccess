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

public class PrK {

    public static final HciOsigR2048 HP_OSIG_R2048 = new HciOsigR2048();
    public static final HciAutR2048 HP_AUT_R2048 = new HciAutR2048();
    public static final HciEncR2048 HP_ENC_R2048 = new HciEncR2048();
    public static final HciOsigE256 HP_OSIG_E256 = new HciOsigE256();
    public static final HciAutE256 HP_AUT_E256 = new HciAutE256();
    public static final HciEncE256 HP_ENC_E256 = new HciEncE256();

    public static class HciOsigR2048 {
        public static final int KID = 0x04;
    }

    public static class HciAutR2048 {
        public static final int KID = 0x02;
    }

    public static class HciEncR2048 {
        public static final int KID = 0x03;
    }

    public static class HciOsigE256 {
        public static final int KID = 0x07;
    }

    public static class HciAutE256 {
        public static final int KID = 0x06;
    }

    public static class HciEncE256 {
        public static final int KID = 0x05;
    }
}
