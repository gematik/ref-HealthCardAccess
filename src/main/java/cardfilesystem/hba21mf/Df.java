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

package cardfilesystem.hba21mf;

import cardfilesystem.hba21mf.df.esign.PrK;
import cardfilesystem.hba21mf.df.qes.Pin;

public class Df {

    public static final Hpa HPA = new Hpa();
    public static final Esign ESIGN = new Esign();
    public static final CiaQes CIA_QES = new CiaQes();
    public static final CiaEsign CIA_ESIGN = new CiaEsign();
    public static final Qes QES = new Qes();
    public static final Auto AUTO = new Auto();

    public static class Hpa {
        public static final String AID = "D27600014602";

        public static final cardfilesystem.hba21mf.df.hpa.Ef EF = new cardfilesystem.hba21mf.df.hpa.Ef();
    }

    public static class Esign {
        public static final String AID = "A000000167455349474E";

        public static final cardfilesystem.hba21mf.df.esign.Ef EF = new cardfilesystem.hba21mf.df.esign.Ef();
        public static final cardfilesystem.hba21mf.df.esign.PrK PrK = new PrK();
    }

    public static class CiaQes {
        public static final String AID = "E828BD080FD27600006601";

        public static final cardfilesystem.hba21mf.df.ciaqes.Ef EF = new cardfilesystem.hba21mf.df.ciaqes.Ef();
    }

    public static class CiaEsign {
        public static final String AID = "E828BD080FA000000167455349474E";

        public static final cardfilesystem.hba21mf.df.ciaesign.Ef EF = new cardfilesystem.hba21mf.df.ciaesign.Ef();
    }

    public static class Qes {
        public static final String AID = "D27600006601";

        public static final cardfilesystem.hba21mf.df.qes.Ef EF = new cardfilesystem.hba21mf.df.qes.Ef();
        public static final Pin PIN = new Pin();
        public static final cardfilesystem.hba21mf.df.qes.PrK PrK = new cardfilesystem.hba21mf.df.qes.PrK();
    }

    public static class Auto {
        public static final String AID = "D27600014603";

        public static final cardfilesystem.hba21mf.df.auto.Ef EF = new cardfilesystem.hba21mf.df.auto.Ef();
        public static final cardfilesystem.hba21mf.df.auto.Pin PIN = new cardfilesystem.hba21mf.df.auto.Pin();
        public static final cardfilesystem.hba21mf.df.auto.PrK PrK = new cardfilesystem.hba21mf.df.auto.PrK();
    }

}
