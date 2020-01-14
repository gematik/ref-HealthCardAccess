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

package cardfilesystem.egk21mf.df.hca.df.amts;

public class Ef {

    public static final Amts AMTS = new Amts();
    public static final VersweiseAmts VerweiseAMTS = new VersweiseAmts();
    public static final StatusAmts StatusAMTS = new StatusAmts();

    public static class Amts {
        public static final int FID = 0xE005;
        public static final int SFID = 0x05;
    }

    public static class VersweiseAmts {
        public static final int FID = 0xE006;
        public static final int SFID = 0x06;
    }

    public static class StatusAmts {
        public static final int FID = 0xE007;
        public static final int SFID = 0x07;
    }
}
