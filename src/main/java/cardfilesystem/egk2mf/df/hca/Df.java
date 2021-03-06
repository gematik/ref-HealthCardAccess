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

public class Df {

    public static final Nfd NFD = new Nfd();
    public static final Dpe DPE = new Dpe();
    public static final Gdd GDD = new Gdd();
    public static final Ose OSE = new Ose();
    public static final Amts AMTS = new Amts();

    public static class Nfd {
        public static final String AID = "D27600014407";

        public static final cardfilesystem.egk2mf.df.hca.df.nfd.Ef EF = new cardfilesystem.egk2mf.df.hca.df.nfd.Ef();
        public static final cardfilesystem.egk2mf.df.hca.df.nfd.MrPin MRPIN = new cardfilesystem.egk2mf.df.hca.df.nfd.MrPin();
    }

    public static class Dpe {
        public static final String AID = "D27600014408";

        public static final cardfilesystem.egk2mf.df.hca.df.dpe.Ef EF = new cardfilesystem.egk2mf.df.hca.df.dpe.Ef();
        public static final cardfilesystem.egk2mf.df.hca.df.dpe.MrPin MRPIN = new cardfilesystem.egk2mf.df.hca.df.dpe.MrPin();
    }

    public static class Gdd {
        public static final String AID = "D2760001440A";

        public static final cardfilesystem.egk2mf.df.hca.df.gdd.Ef EF = new cardfilesystem.egk2mf.df.hca.df.gdd.Ef();
        public static final cardfilesystem.egk2mf.df.hca.df.gdd.MrPin MRPIN = new cardfilesystem.egk2mf.df.hca.df.gdd.MrPin();
    }

    public static class Ose {
        public static final String AID = "D2760001440B";

        public static final cardfilesystem.egk2mf.df.hca.df.ose.Ef EF = new cardfilesystem.egk2mf.df.hca.df.ose.Ef();
        public static final cardfilesystem.egk2mf.df.hca.df.ose.MrPin MRPIN = new cardfilesystem.egk2mf.df.hca.df.ose.MrPin();
    }

    public static class Amts {
        public static final String AID = "D2760001440C";

        public static final cardfilesystem.egk2mf.df.hca.df.amts.Ef EF = new cardfilesystem.egk2mf.df.hca.df.amts.Ef();
        public static final cardfilesystem.egk2mf.df.hca.df.amts.MrPin MRPIN = new cardfilesystem.egk2mf.df.hca.df.amts.MrPin();
        public static final cardfilesystem.egk2mf.df.hca.df.amts.Pin PIN = new cardfilesystem.egk2mf.df.hca.df.amts.Pin();
        public static final cardfilesystem.egk2mf.df.hca.df.amts.PrK PrK = new cardfilesystem.egk2mf.df.hca.df.amts.PrK();

    }
}
