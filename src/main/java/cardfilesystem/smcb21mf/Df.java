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

package cardfilesystem.smcb21mf;

public class Df {

    public static final Esign ESIGN = new Esign();

    public static class Esign {
        public static final String AID = "A000000167455349474E";

        public static final cardfilesystem.smcb21mf.df.esign.Ef EF = new cardfilesystem.smcb21mf.df.esign.Ef();
        public static final cardfilesystem.smcb21mf.df.esign.PrK PrK = new cardfilesystem.smcb21mf.df.esign.PrK();
    }
}
