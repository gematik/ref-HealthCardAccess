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

package cardfilesystem.egk21mf;

public class Ef {

    public static final Atr ATR = new Atr();
    public static final CardAccess CardAccess = new CardAccess();
    public static final CcaEgkCsE256 C_CA_EGK_CS_E256 = new CcaEgkCsE256();
    public static final CegkAutCvcE256 C_EGK_AUT_CVC_E256 = new CegkAutCvcE256();
    public static final Dir DIR = new Dir();
    public static final Gdo GDO = new Gdo();
    public static final Version Version = new Version();
    public static final Version2 Version2 = new Version2();

    public static class Atr {
        public static final int FID = 0x2F01;
        public static final int SFID = 0x1D;
    }

    public static class CardAccess {
        public static final int FID = 0x011C;
        public static final int SFID = 0x1C;
    }

    public static class CcaEgkCsE256 {
        public static final int FID = 0x2F07;
        public static final int SFID = 0x07;
    }

    public static class CegkAutCvcE256 {
        public static final int FID = 0x2F06;
        public static final int SFID = 0x06;
    }

    public static class Dir {
        public static final int FID = 0x2F00;
        public static final int SFID = 0x1E;
    }

    public static class Gdo {
        public static final int FID = 0x2F02;
        public static final int SFID = 0x02;
    }

    public static class Version {
        public static final int FID = 0x2F10;
        public static final int SFID = 0x10;
    }

    public static class Version2 {
        public static final int FID = 0x2F11;
        public static final int SFID = 0x11;
    }
}
