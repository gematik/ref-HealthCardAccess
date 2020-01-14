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

public class PrK {

    public static final SmcAutrCvcR2048 SMC_AUTR_CVC_R2048 = new SmcAutrCvcR2048();
    public static final SmcAutrCvcE256 SMC_AUTR_CVC_E256 = new SmcAutrCvcE256();
    public static final SmcAutdSukCvcE256 SMC_AUTD_SUK_CVC_E256 = new SmcAutdSukCvcE256();

    public static class SmcAutrCvcR2048 {
        public static final int KID = 0x10;
    }

    public static class SmcAutrCvcE256 {
        public static final int KID = 0x06;
    }

    public static class SmcAutdSukCvcE256 {
        public static final int KID = 0x09;
    }
}
