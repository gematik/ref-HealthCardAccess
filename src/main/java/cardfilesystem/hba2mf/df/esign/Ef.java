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

package cardfilesystem.hba2mf.df.esign;

public class Ef {

    public static final ChpAutR2048 C_CH_AUT_R2048 = new ChpAutR2048();
    public static final ChpEncR2048 C_CH_ENC_R2048 = new ChpEncR2048();

    public static class ChpAutR2048 {
        public static final int FID = 0xC500;
        public static final int SFID = 0x01;
    }

    public static class ChpEncR2048 {
        public static final int FID = 0xC200;
        public static final int SFID = 0x02;
    }
}
