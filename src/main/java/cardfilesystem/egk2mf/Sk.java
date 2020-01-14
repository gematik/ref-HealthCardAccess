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

package cardfilesystem.egk2mf;

public class Sk {

    public static final CmsAes128 CMS_AES128 = new CmsAes128();
    public static final CmsAes256 CMS_AES256 = new CmsAes256();
    public static final VsdAes128 VSD_AES128 = new VsdAes128();
    public static final VsdAes256 VSD_AES256 = new VsdAes256();
    public static final Can CAN = new Can();

    public interface ICMSSymkey {
        int getKID();
    }

    public interface IVSDSymkey {
        int getKID();
    }

    public static class CmsAes128 implements ICMSSymkey {
        private static final int KID = 0x13;

        @Override
        public int getKID() {
            return KID;
        }
    }

    public static class CmsAes256 implements ICMSSymkey {
        private static final int KID = 0x18;

        @Override
        public int getKID() {
            return KID;
        }
    }

    public static class VsdAes128 implements IVSDSymkey {
        private static final int KID = 0x12;

        @Override
        public int getKID() {
            return KID;
        }
    }

    public static class VsdAes256 implements IVSDSymkey {
        private static final int KID = 0x19;

        @Override
        public int getKID() {
            return KID;
        }
    }

    public static class Can {
        public static final int KID = 0x02;
    }
}
