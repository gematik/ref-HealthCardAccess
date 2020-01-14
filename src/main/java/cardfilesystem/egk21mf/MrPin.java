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

public class MrPin {

    public static final Home home = new Home();
    public static final Nfd NFD = new Nfd();
    public static final Dpe DPE = new Dpe();
    public static final Gdd GDD = new Gdd();
    public static final NfdRead NFD_READ = new NfdRead();
    public static final Ose OSE = new Ose();
    public static final Amts AMTS = new Amts();

    public static class Home {
        public static final int PWID = 0x02;
    }

    public static class Nfd {
        public static final int PWID = 0x03;
    }

    public static class Dpe {
        public static final int PWID = 0x04;
    }

    public static class Gdd {
        public static final int PWID = 0x05;
    }

    public static class NfdRead {
        public static final int PWID = 0x07;
    }

    public static class Ose {
        public static final int PWID = 0x09;
    }

    public static class Amts {
        public static final int PWID = 0x0C;
    }
}
