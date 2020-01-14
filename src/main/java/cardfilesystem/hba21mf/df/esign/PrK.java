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

package cardfilesystem.hba21mf.df.esign;

public class PrK {

    public static final HpAutR2048 HP_AUT_R2048 = new HpAutR2048();
    public static final HpAutnR2048 HP_AUTN_R2048 = new HpAutnR2048();
    public static final HpEncR2048 HP_ENC_R2048 = new HpEncR2048();
    public static final HpEncvR2048 HP_ENCV_R2048 = new HpEncvR2048();
    public static final HpAutE256 HP_AUT_E256 = new HpAutE256();
    public static final HpAutnE256 HP_AUTN_E256 = new HpAutnE256();
    public static final HpEncE256 HP_ENC_E256 = new HpEncE256();
    public static final HpEncvE256 HP_ENCV_E256 = new HpEncvE256();

    public static class HpAutR2048 {
        public static final int KID = 0x02;
    }

    public static class HpAutnR2048 {
        public static final int KID = 0x06;
    }

    public static class HpEncR2048 {
        public static final int KID = 0x03;
    }

    public static class HpEncvR2048 {
        public static final int KID = 0x07;
    }

    public static class HpAutE256 {
        public static final int KID = 0x04;
    }

    public static class HpAutnE256 {
        public static final int KID = 0x0B;
    }

    public static class HpEncE256 {
        public static final int KID = 0x05;
    }

    public static class HpEncvE256 {
        public static final int KID = 0x0C;
    }
}
