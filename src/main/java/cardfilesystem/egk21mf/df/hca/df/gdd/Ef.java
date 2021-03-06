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

package cardfilesystem.egk21mf.df.hca.df.gdd;

public class Ef {

    public static final EinwilligungGdd EinwilligungGDD = new EinwilligungGdd();
    public static final VerweiseGdd VerweiseGDD = new VerweiseGdd();

    public static class EinwilligungGdd {
        public static final int FID = 0xD013;
        public static final int SFID = 0x13;
    }

    public static class VerweiseGdd {
        public static final int FID = 0xD01A;
        public static final int SFID = 0x1A;
    }
}
