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

import cardfilesystem.egk21mf.df.esign.PrK;
import cardfilesystem.egk21mf.df.hca.Ef;
import cardfilesystem.egk21mf.df.qes.Pin;

public class Df {

    public static final Hca HCA = new Hca();
    public static final Esign ESIGN = new Esign();
    public static final CiaEsign CIA_ESIGN = new CiaEsign();
    public static final Qes QES = new Qes();

    public static class Hca {
        public static final String AID = "D27600000102";

        public static final cardfilesystem.egk21mf.df.hca.Df DF = new cardfilesystem.egk21mf.df.hca.Df();
        public static final Ef EF = new Ef();
    }

    public static class Esign {
        public static final String AID = "A000000167455349474E";

        public static final cardfilesystem.egk21mf.df.esign.Ef EF = new cardfilesystem.egk21mf.df.esign.Ef();
        public static final cardfilesystem.egk21mf.df.esign.PrK PrK = new PrK();
    }

    public static class CiaEsign {
        public static final String AID = "E828BD080FA000000167455349474E";

        public static final cardfilesystem.egk21mf.df.ciaesign.Ef EF = new cardfilesystem.egk21mf.df.ciaesign.Ef();
    }

    public static class Qes {
        public static final String AID = "D27600006601";

        public static final cardfilesystem.egk21mf.df.qes.Ef EF = new cardfilesystem.egk21mf.df.qes.Ef();
        public static final Pin PIN = new Pin();
        public static final cardfilesystem.egk21mf.df.qes.PrK PrK = new cardfilesystem.egk21mf.df.qes.PrK();
    }
}
