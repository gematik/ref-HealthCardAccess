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

package cardfilesystem.egk21mf.df.esign;

public class PrK {

    public static final ChAutE256 CH_AUT_E256 = new ChAutE256();
    public static final ChAutnE256 CH_AUTN_E256 = new ChAutnE256();
    public static final ChEncE256 CH_ENC_E256 = new ChEncE256();
    public static final ChEncvE256 CH_ENCV_E256 = new ChEncvE256();

    public static final ChAutR2048 CH_AUT_R2048 = new ChAutR2048();
    public static final ChAutnR2048 CH_AUTN_R2048 = new ChAutnR2048();
    public static final ChEncR2048 CH_ENC_R2048 = new ChEncR2048();
    public static final ChEncvR2048 CH_ENCV_R2048 = new ChEncvR2048();

    public static class ChAutR2048 {
        public static final int KID = 0x02;
    }

    public static class ChAutnR2048 {
        public static final int KID = 0x06;
    }

    public static class ChEncR2048 {
        public static final int KID = 0x03;
    }

    public static class ChEncvR2048 {
        public static final int KID = 0x07;
    }

    public static class ChAutE256 {
        public static final int KID = 0x04;
    }

    public static class ChAutnE256 {
        public static final int KID = 0x0B;
    }

    public static class ChEncE256 {
        public static final int KID = 0x05;
    }

    public static class ChEncvE256 {
        public static final int KID = 0x0C;
    }

}
