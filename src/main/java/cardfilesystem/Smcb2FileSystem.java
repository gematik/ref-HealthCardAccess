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

import cardfilesystem.smcb2mf.Df;
import cardfilesystem.smcb2mf.Ef;
import cardfilesystem.smcb2mf.Pin;
import cardfilesystem.smcb2mf.PrK;
import cardfilesystem.smcb2mf.Sk;

/**
 * Entry point to card file system layout for SMC-B G2 smart cards.
 */
public class Smcb2FileSystem {
    public static final String AID = "D27600014606";
    public static final int FID = 0x3F00;

    public static final Ef EF = new Ef();
    public static final Df DF = new Df();
    public static final Pin PIN = new Pin();
    public static final PrK PrK = new PrK();
    public static final Sk SK = new Sk();
}
