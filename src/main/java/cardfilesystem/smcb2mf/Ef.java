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

package cardfilesystem.smcb2mf;

public class Ef {

    public static final Atr ATR = new Atr();
    public static final Dir DIR = new Dir();
    public static final Gdo GDO = new Gdo();
    public static final Version2 Version2 = new Version2();
    public static final CcaSmcCsR2048 C_CA_SMC_CS_R2048 = new CcaSmcCsR2048();
    public static final CcaSmcCsE256 C_CA_SMC_CS_E256 = new CcaSmcCsE256();
    public static final CsmcAutrCvcR2048 C_SMC_AUTR_CVC_R2048 = new CsmcAutrCvcR2048();
    public static final CsmcAutrCvcE256 C_SMC_AUTR_CVC_E256 = new CsmcAutrCvcE256();
    public static final CsmcAutdRpeCvcE256 C_SMC_AUTD_RPE_CVC_E256 = new CsmcAutdRpeCvcE256();

    public static class Atr {
        public static final int FID = 0x2F01;
        public static final int SFID = 0x1D;
    }

    public static class Dir {
        public static final int FID = 0x2F00;
        public static final int SFID = 0x1E;
    }

    public static class Gdo {
        public static final int FID = 0x2F02;
        public static final int SFID = 0x02;
    }

    public static class Version2 {
        public static final int FID = 0x2F11;
        public static final int SFID = 0x11;
    }

    public static class CcaSmcCsR2048 {
        public static final int FID = 0x2F04;
        public static final int SFID = 0x04;
    }

    public static class CcaSmcCsE256 {
        public static final int FID = 0x2F07;
        public static final int SFID = 0x07;
    }

    public static class CsmcAutrCvcR2048 {
        public static final int FID = 0x2F03;
        public static final int SFID = 0x03;
    }

    public static class CsmcAutrCvcE256 {
        public static final int FID = 0x2F06;
        public static final int SFID = 0x06;
    }

    public static class CsmcAutdRpeCvcE256 {
        public static final int FID = 0x2F09;
        public static final int SFID = 0x09;
    }
}
