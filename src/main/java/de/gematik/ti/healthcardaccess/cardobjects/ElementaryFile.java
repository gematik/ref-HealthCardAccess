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

package de.gematik.ti.healthcardaccess.cardobjects;

import de.gematik.ti.healthcardaccess.ICardItem;

/**
 *
 * The EFs store data. An EF cannot be the parent of another file. Two categories of EFs are specified.
 * • An internal EF stores data interpreted by the card, i.e., data used by the card for management and
 * control purposes.
 * • A working EF stores data not interpreted by the card, i.e., data used by the outside world exclusively
 * @see "ISO/IEC 7816-4"
 *
 * @created 05-Jul-2018 14:33:51
 */
public class ElementaryFile implements ICardItem {

    private final FileIdentifier fid;
    private final ShortFileIdentifier sfid;

    public ElementaryFile(final FileIdentifier fid) {
        this(fid, null);
    }

    public ElementaryFile(final FileIdentifier fid, final ShortFileIdentifier sfid) {
        this.fid = fid;
        this.sfid = sfid;
    }
}
