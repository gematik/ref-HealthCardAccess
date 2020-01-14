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

package cardfilesystem.hba2mf.df.qes;

public class Ef {

    public static final Ssec SSEC = new Ssec();
    public static final ChpQesR2048 C_HP_QES_R2048 = new ChpQesR2048();
    public static final ChpQesAc1 C_HP_QES_AC1 = new ChpQesAc1();
    public static final ChpQesAc2 C_HP_QES_AC2 = new ChpQesAc2();
    public static final ChpQesAc3 C_HP_QES_AC3 = new ChpQesAc3();

    public static class Ssec {
        public static final int FID = 0xD005;
        public static final int SFID = 0x05;
    }

    public static class ChpQesR2048 {
        public static final int FID = 0xC000;
        public static final int SFID = 0x10;
    }

    public static class ChpQesAc1 {
        public static final int FID = 0xC001;
        public static final int SFID = 0x01;
    }

    public static class ChpQesAc2 {
        public static final int FID = 0xC002;
        public static final int SFID = 0x02;
    }

    public static class ChpQesAc3 {
        public static final int FID = 0xC003;
        public static final int SFID = 0x03;
    }
}
