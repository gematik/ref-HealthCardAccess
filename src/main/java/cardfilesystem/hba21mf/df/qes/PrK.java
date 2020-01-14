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

package cardfilesystem.hba21mf.df.qes;

public class PrK {

    public static final HpQesR2048 HP_QES_R2048 = new HpQesR2048();
    public static final HpQesE256 HP_QES_E256 = new HpQesE256();

    public static class HpQesR2048 {
        public static final int KID = 0x04;
    }

    public static class HpQesE256 {
        public static final int KID = 0x06;
    }
}
