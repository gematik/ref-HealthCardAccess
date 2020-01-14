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

package cardfilesystem.hba21mf.df.auto;

public class Ef {

    public static final ChpAuto1R3072 C_HP_AUTO1_R3072 = new ChpAuto1R3072();
    public static final ChpAuto2R3072 C_HP_AUTO2_R3072 = new ChpAuto2R3072();

    public static class ChpAuto1R3072 {
        public static final int FID = 0xE001;
        public static final int SFID = 0x01;
    }

    public static class ChpAuto2R3072 {
        public static final int FID = 0xE002;
        public static final int SFID = 0x02;
    }
}
