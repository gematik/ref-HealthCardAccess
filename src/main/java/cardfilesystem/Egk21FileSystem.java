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

package cardfilesystem;

import cardfilesystem.egk21mf.Df;
import cardfilesystem.egk21mf.Ef;
import cardfilesystem.egk21mf.MrPin;
import cardfilesystem.egk21mf.Pin;
import cardfilesystem.egk21mf.PrK;
import cardfilesystem.egk21mf.Sk;

/**
 * Entry point to card file system layout for eGK G2.1 smart cards.
 */
public class Egk21FileSystem {
    public static final String AID = "D2760001448000";
    public static final int FID = 0x3F00;

    public static final Ef EF = new Ef();
    public static final Df DF = new Df();
    public static final MrPin MRPIN = new MrPin();
    public static final Pin PIN = new Pin();
    public static final PrK PrK = new PrK();
    public static final Sk SK = new Sk();
}
