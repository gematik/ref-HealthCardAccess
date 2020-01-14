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

public class PrK {

    public static final HpAutR2048 HP_AUT_R2048 = new HpAutR2048();
    public static final HpEncR2048 HP_ENC_R2048 = new HpEncR2048();

    public static class HpAutR2048 {
        public static final int KID = 0x02;
    }

    public static class HpEncR2048 {
        public static final int KID = 0x03;
    }
}
