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

package cardfilesystem.egk2mf.df.hca;

public class Ef {

    public static final Einwilligung Einwilligung = new Einwilligung();
    public static final Gvd GVD = new Gvd();
    public static final Logging Logging = new Logging();
    public static final Pd PD = new Pd();
    public static final Pruefungsnachweis Pruefungsnachweis = new Pruefungsnachweis();
    public static final Standalone Standalone = new Standalone();
    public static final StatusVd StatusVD = new StatusVd();
    public static final Ttn TTN = new Ttn();
    public static final Vd VD = new Vd();
    public static final Verweis Verweis = new Verweis();

    public static class Einwilligung {
        public static final int FID = 0xD005;
        public static final int SFID = 0x05;
    }

    public static class Gvd {
        public static final int FID = 0xD003;
        public static final int SFID = 0x03;
    }

    public static class Logging {
        public static final int FID = 0xD006;
        public static final int SFID = 0x06;
    }

    public static class Pd {
        public static final int FID = 0xD001;
        public static final int SFID = 0x01;
    }

    public static class Pruefungsnachweis {
        public static final int FID = 0xD01C;
        public static final int SFID = 0x1C;
    }

    public static class Standalone {
        public static final int FID = 0xDA0A;
        public static final int SFID = 0x0A;
    }

    public static class StatusVd {
        public static final int FID = 0xD00C;
        public static final int SFID = 0x0C;
    }

    public static class Ttn {
        public static final int FID = 0xD00F;
        public static final int SFID = 0x0F;
    }

    public static class Vd {
        public static final int FID = 0xD002;
        public static final int SFID = 0x02;
    }

    public static class Verweis {
        public static final int FID = 0xD009;
        public static final int SFID = 0x09;
    }
}
