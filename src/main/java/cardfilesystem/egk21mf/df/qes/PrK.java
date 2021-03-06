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

package cardfilesystem.egk21mf.df.qes;

public class PrK {

    public static final ChQesR2048 CH_QES_R2048 = new ChQesR2048();
    public static final ChQesE256 CH_QES_E256 = new ChQesE256();

    public static class ChQesR2048 {
        public static final int KID = 0x04;
    }

    public static class ChQesE256 {
        public static final int KID = 0x06;
    }
}
