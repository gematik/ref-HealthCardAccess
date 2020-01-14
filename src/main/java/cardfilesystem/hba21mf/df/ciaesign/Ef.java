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

package cardfilesystem.hba21mf.df.ciaesign;

public class Ef {

    public static final CiaInfo CIA_Info = new CiaInfo();
    public static final Od OD = new Od();
    public static final Aod AOD = new Aod();
    public static final PrKd PrKD = new PrKd();
    public static final Cd CD = new Cd();

    public static class CiaInfo {
        public static final int FID = 0x5032;
        public static final int SFID = 0x12;
    }

    public static class Od {
        public static final int FID = 0x5031;
        public static final int SFID = 0x11;
    }

    public static class Aod {
        public static final int FID = 0x5034;
        public static final int SFID = 0x14;
    }

    public static class PrKd {
        public static final int FID = 0x5035;
        public static final int SFID = 0x15;
    }

    public static class Cd {
        public static final int FID = 0x5038;
        public static final int SFID = 0x16;
    }
}
