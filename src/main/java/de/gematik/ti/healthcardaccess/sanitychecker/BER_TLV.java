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

package de.gematik.ti.healthcardaccess.sanitychecker;

import java.util.ArrayList;

/**
 * Die Klasse wurde entwickelt, um ein komfortables Arbeiten mit TLV Strukturen zu ermöglichen.
 */

public class BER_TLV { // NOCS(LAC): Spec name

    private static final String WHITESPACE_00 = "00";
    private static final String WHITESPACE_FF = "ff";
    private static final int RADIX_HEX_16 = 16;

    /**
     * Enumeration beinhaltet alle möglichen BER_TLV Klassen:
     * <ul>
     * <li>UNIVERSAL</li>
     * <li>APPLICATION</li>
     * <li>CONTEXT_SPECIFIC</li>
     * <li>PRIVATE</li>
     * <li>NO_TAG</li>
     * <li>WHITESPACE_BLOCK</li>
     * </ul>
     */
    private enum BER_TLV_Class {
        UNIVERSAL,
        APPLICATION,
        CONTEXT_SPECIFIC,
        PRIVATE,
        NO_TAG,
        WHITESPACE_BLOCK
    }

    // private static final int B87_00 = 0x00; //universal
    private static final int B87_01 = 0x40; // application
    private static final int B87_10 = 0x80; // context specific
    private static final int B87_11 = 0xC0; // private
    private static final int B8_1 = 0x80;
    private static final int B51_11111 = 0x1F; // the tag is spread over more than only the first byte.

    private static final int B6_1 = 0x20; // constructed data object

    private String tag;
    private String length;

    private boolean isEmptyRoot;
    private boolean isWhitespaceBlock;

    private final ArrayList<BER_TLV> enclosedTLVs;

    private String value;

    public BER_TLV() {
        isEmptyRoot = false;
        isWhitespaceBlock = false;
        tag = "";
        length = WHITESPACE_00;
        value = "";
        enclosedTLVs = new ArrayList<>();
    }

    public BER_TLV(final String tag, final String value) {
        this();
        checkTag(tag);
        this.tag = tag;

        this.value = value;
        recalculateLength();
        if (isConstructedDO()) {
            parseBERTLV(this.toString());
        }
    }

    /**
     * Der Konstruktor
     * 
     * @param tlv
     *            - Das TLV-codierte Objekt in Textform
     */
    public BER_TLV(final String tlv) {
        this();
        checkTlv(tlv);
        String tmpTlv = tlv;
        int tlvSize = checkForTLV(tmpTlv);
        int blankLength = getBlankLength(tmpTlv);
        if (tlvSize > 0 && tlvSize < tmpTlv.length()) {
            isEmptyRoot = true;
            length = "";
            do {
                if (blankLength > 0) {
                    enclosedTLVs.add(new BER_TLV(tmpTlv.substring(0, blankLength), blankLength));
                    tmpTlv = tmpTlv.substring(blankLength);
                }
                enclosedTLVs.add(new BER_TLV(tmpTlv.substring(0, tlvSize)));
                tmpTlv = tmpTlv.substring(tlvSize);
                tlvSize = checkForTLV(tmpTlv);
            } while (tlvSize > 0);
        } else {
            parseBERTLV(tmpTlv);
        }
    }

    private void checkTlv(String tlv) {
        if (tlv == null) {
            throw new IllegalArgumentException("The given TLV String is null");
        }
    }

    /**
     * Konstruktor wird genutzt um Leerzeichen Blöcke aus einem BER_TLV Objekt zu entfernen.
     * 
     * @param whitespaceBlock
     *            - Die Zeichen, die für Leerzeichen Blöcke genutzt werden ('00' oder 'FF')
     * @param blockLength
     *            - Die Blocklänge der Leerzeichen
     */
    public BER_TLV(final String whitespaceBlock, final int blockLength) {

        String tmpWhitespaceBlock = whitespaceBlock;
        int tmpBlockLength = blockLength;

        if (!tmpWhitespaceBlock.matches("((00)|(FF))+")) {
            throw new IllegalArgumentException(
                    getClass().getName() + " : Not a valid whitespace block (allowed whitespaces are 00, FF) : " + tmpWhitespaceBlock);
        }

        while (tmpWhitespaceBlock.length() < tmpBlockLength) {
            tmpWhitespaceBlock = tmpWhitespaceBlock.substring(0, 2) + tmpWhitespaceBlock;
        }
        if (tmpBlockLength % 2 != 0) {
            tmpBlockLength += 1;
        }
        if (tmpWhitespaceBlock.length() > tmpBlockLength) {
            tmpWhitespaceBlock = tmpWhitespaceBlock.substring(0, tmpBlockLength);
        }

        isWhitespaceBlock = true;
        isEmptyRoot = false;

        tag = "";
        length = "";
        value = tmpWhitespaceBlock;
        enclosedTLVs = new ArrayList<>();
    }

    /**
     * Methode kategorisiert das Tag des aktuellen Objekts.
     * 
     * @return Die BER_TLV Klasse (siehe auch {@link BER_TLV_Class})
     */
    public BER_TLV_Class categorizeTag() {
        if (isEmptyRoot) {
            return BER_TLV_Class.NO_TAG;
        }
        if (isWhitespaceBlock) {
            return BER_TLV_Class.WHITESPACE_BLOCK;
        }

        int localTag = Integer.parseInt(tag.substring(0, 2), RADIX_HEX_16);

        if ((localTag & B87_11) == B87_11) {
            return BER_TLV_Class.PRIVATE;
        }
        if ((localTag & B87_10) == B87_10) {
            return BER_TLV_Class.CONTEXT_SPECIFIC;
        }
        if ((localTag & B87_01) == B87_01) {
            return BER_TLV_Class.APPLICATION;
        }
        return BER_TLV_Class.UNIVERSAL;
    }

    /**
     * Methode prüft, ob DO-Tag vorhanden ist
     * 
     * @return <b>true</b> wenn Tag vorhanden ist, sonst <b>false</b>.
     */
    public boolean isConstructedDO() {
        if (isEmptyRoot || isWhitespaceBlock) {
            return false;
        }
        int localTag = Integer.parseInt(tag.substring(0, 2), RADIX_HEX_16);
        return (localTag & B6_1) != 0;
    }

    /**
     * @return Das Tag in hexadezimaler Darstellung.
     */
    public String getTag() {
        return this.tag.toUpperCase();
    }

    /**
     * @return Die Länge (in Abhängigkeit zu ASN.1)<br>
     *         z.bsp.: '08', '8202F1'
     */
    public String getLength() {
        return length.toUpperCase();
    }

    /**
     * @return Die Länge<br>
     *         z.bsp: 8, 753
     */
    public int getLengthInt() {
        return getLengthAsInt(length);
    }

    public String getValue() {
        return value.toUpperCase();
    }

    /**
     * @return Das TLV Objekt in hexadezimaler Darstellung.
     */
    @Override
    public String toString() {
        if (tag.isEmpty() && length.isEmpty() && value.isEmpty()) {
            String tlv = "";
            for (BER_TLV child : enclosedTLVs) {
                tlv += child.toString();
            }
            return tlv;
        } else {
            return (tag + length + value).toUpperCase();
        }
    }

    /**
     * @deprecated Bitte {@link BER_TLV#toString()} nutzen
     */
    @Deprecated
    public String toTlvString() {
        return toString();
    }

    public void setTag(final String tag) {
        checkTag(tag);
        this.tag = tag;
    }

    public void setValue(final String value) {
        this.value = value;
        recalculateLength();
        if (isConstructedDO()) {
            parseBERTLV(this.toString());
        }
    }

    /**
     * Hängt einen neuen Wert an einen existierenden Wert an.
     * 
     * @param value
     *            - Der neue Wert in Hexadezimal<br>
     *            <br>
     *            z.bsp.: start TLV: '84 04 82 02 22 22'<br>
     *            appendToValue('81 01 11')<br>
     *            neue TLV Struktur: '84 07 82 02 22 22 81 01 11'
     */
    public void appendToValue(final String value) {
        String tmpValue = value;
        if (tmpValue == null) {
            tmpValue = "";
        }

        this.value += tmpValue;
        recalculateLength();
        if (isConstructedDO()) {
            enclosedTLVs.add(new BER_TLV(tmpValue));
        }
    }

    /**
     * Methode sucht ein Objekt anhand seines Tag namen.
     * 
     * @param tagName
     *            - Der zu suchene Tag name.
     * @return Das gefundene Objekt oder <b>null</b> wenn nichts gefunden wurde.
     */
    public BER_TLV findTag(final String tagName) {
        if (this.tag.equalsIgnoreCase(tagName)) {
            return this;
        }

        for (int i = 0; i < enclosedTLVs.size(); i++) {
            BER_TLV tlv = enclosedTLVs.get(i).findTag(tagName);
            if (tlv != null) {
                return tlv;
            }
        }
        return null;
    }

    /**
     * Methode sucht nach einem Objekt anahnd seinenm Pfades.
     * 
     * @param path
     *            - Ein Array mit Tag names welche den Pfad zu dem gewünschten Pfad beinhalten.
     * @return Das gefundene Objekt oder <b>null</b> wenn nichts gefunden wurde.
     */
    public BER_TLV getTlv(final String[] path) {
        if (!path[0].equals(this.getTag())) {
            return null;
        }
        if (path.length == 1) {
            return this;
        }

        String[] newPath = new String[path.length - 1];
        System.arraycopy(path, 1, newPath, 0, path.length - 1);
        for (BER_TLV tlv : enclosedTLVs) {
            if (tlv.getTag().equals(newPath[0])) {
                return tlv.getTlv(newPath);
            }
        }
        return null;
    }

    /**
     * Erstellt die Textrepräsentation des aktuellen TLV.
     * 
     * @param depth
     *            - Das 'Level' des aktuell erzeugten TLV Objekts.
     * @return Die generierte Textrepräsentation
     */
    public String printTLVStructure(final int depth) {
        String out = "";
        if (isWhitespaceBlock) {
            return getXTimes("\t", depth) + value + "\n";
        }
        if (!isEmptyRoot) {
            out += getXTimes("\t", depth) + tag + "\n";
            // out += getXTimes("\t", depth) + "\t" ; //+ length
        }
        // else
        // --depth;

        if (enclosedTLVs.size() > 0) {
            for (int i = 0; i < enclosedTLVs.size(); i++) {
                out += enclosedTLVs.get(i).printTLVStructure(depth + 1);
            }
        } else {
            out += getXTimes("\t", depth + 1) + "" + value + "\n";
        }
        return out;
    }

    private void checkTag(final String tag) {
        if (tag == null || tag.length() < 1 || tag.toLowerCase().equals(WHITESPACE_FF) || tag.equals(WHITESPACE_00)) {
            throw new IllegalArgumentException(getClass().getName() + " : Invalid tag detected - " + tag);
        }
    }

    /**
     * Methode ersetzt das neue BER_TLV Objekt mit dem alten.
     * 
     * @param berTlv
     *            - Das BER_TLV Objekt in hexadezimaler Darstellung
     */
    private void parseBERTLV(final String berTlv) {
        if (berTlv == null || berTlv.isEmpty()) {
            tag = "";
            length = "";
            value = "";
            return;
        }
        this.enclosedTLVs.clear();
        int blanks = getBlankLength(berTlv);
        if (blanks > 0) {
            if (blanks != berTlv.length()) {
                throw new IllegalArgumentException(getClass().getName() + " : Not a valid whitespace block (allowed whitespaces are 00, FF) : " + berTlv);
            }
            isEmptyRoot = false;
            isWhitespaceBlock = true;
            this.tag = "";
            this.length = "";
            this.value = berTlv;
            return;
        }
        String tmp = berTlv.substring(0, 2);
        int localTag = Integer.parseInt(tmp, RADIX_HEX_16);
        int currentIndex = 2;
        if ((localTag & B51_11111) == B51_11111) {
            do {
                tmp = berTlv.substring(currentIndex, currentIndex + 2);
                localTag = Integer.parseInt(tmp, RADIX_HEX_16);
                currentIndex += 2;
            } while ((localTag & B8_1) != 0);
        }
        tag = berTlv.substring(0, currentIndex);
        checkTag(tag);
        tmp = berTlv.substring(currentIndex, currentIndex + 2);
        int tmpInt = Integer.parseInt(tmp, RADIX_HEX_16);
        if ((tmpInt & 0x80) != 0) {
            tmpInt = tmpInt ^ 0x80;
            tmp = berTlv.substring(currentIndex, currentIndex + 2 + 2 * tmpInt); // +2 for '8x' and x times 2 for the
                                                                                 // number of the following bytes
            currentIndex = currentIndex + 2 + 2 * tmpInt;
        } else {
            currentIndex += 2;
        }
        length = tmp;
        value = berTlv.substring(currentIndex, currentIndex + 2 * getLengthInt());
        // Check value field only if Tag indicates a constructed TLV Object
        if (isConstructedDO()) {
            tmp = value;
            tmpInt = checkForTLV(tmp);
            int tmpInt2 = getBlankLength(tmp);
            while (tmpInt > 0 || tmpInt2 > 0) {
                if (tmpInt2 > 0) {
                    enclosedTLVs.add(new BER_TLV(tmp.substring(0, tmpInt2), tmpInt2));
                    tmp = tmp.substring(tmpInt2);
                } else {
                    enclosedTLVs.add(new BER_TLV(tmp.substring(0, tmpInt)));
                    tmp = tmp.substring(tmpInt);
                }
                tmpInt = checkForTLV(tmp);
                tmpInt2 = getBlankLength(tmp);
            }
        }
    }

    private String getXTimes(final String toRepeat, final int times) {
        String out = "";
        for (int i = 0; i < times; i++) {
            out += toRepeat;
        }
        return out;
    }

    /**
     * Methode kalkuliert und speichert die Länge des aktuellen Werts.
     */
    private void recalculateLength() {
        if (this.value == null) {
            this.length = WHITESPACE_00;
            return;
        }
        int len = this.value.length() / 2;
        String lenString = makeEven(Integer.toHexString(len));
        if (len <= 127) {
            this.length = lenString;
            return;
        }
        len = lenString.length() / 2;
        len = len | 0x80;
        this.length = Integer.toHexString(len) + lenString;

    }

    /**
     * @param length
     *            - Das Tag mit der Länge
     * @return Der Wert der Länge in Integer
     */
    private int getLengthAsInt(final String length) {
        if (length.length() > 2) {
            return Integer.parseInt(length.substring(2), RADIX_HEX_16);
        }

        return Integer.parseInt(length, RADIX_HEX_16);

    }

    public ArrayList<BER_TLV> getChildren() {
        return enclosedTLVs;
    }

    public void addChild(final BER_TLV child) {
        appendToValue(child.toTlvString());
        recalculateLength();
    }

    /**
     * Methode überprüft, ob der übergebene Wert in TLV Struktur vorliegt oder damit beginnt (Führende Leerzeichen
     * wie '00' oder 'FF' werden hierbei ignoriert).
     * 
     * @param toTest
     *            - Der zu überprüfende Wert
     * @return Die Länge des ersten gefundenen TLV Objekts, oder <b>-1</b> wenn kein TLV Objekt vorhanden ist.
     */
    private int checkForTLV(final String toTest) {
        if (toTest == null || toTest.length() < 4) {
            return -1;
        }
        try {
            String tmp = toTest.substring(0, 2);
            int currentIndex = 2;
            int whiteSpaceCount = 0;
            while (tmp.equalsIgnoreCase(WHITESPACE_FF) || tmp.equalsIgnoreCase(WHITESPACE_00)) {
                tmp = toTest.substring(currentIndex, currentIndex + 2);
                currentIndex += 2;
                whiteSpaceCount += 2;
            }
            int localTag = Integer.parseInt(tmp, RADIX_HEX_16);
            if ((localTag & B51_11111) == B51_11111) {
                do {
                    tmp = toTest.substring(currentIndex, currentIndex + 2);
                    localTag = Integer.parseInt(tmp, RADIX_HEX_16);
                    currentIndex += 2;
                } while ((localTag & B8_1) != 0);
            }
            // sonarCube finds out the followinng parseInt never used
            // Integer.parseInt(tmp, RADIX_HEX_16);
            tmp = toTest.substring(currentIndex, currentIndex + 2);
            int tmpInt = Integer.parseInt(tmp, RADIX_HEX_16);
            if ((tmpInt & 0x80) != 0) {
                tmpInt = tmpInt ^ 0x80;
                tmp = toTest.substring(currentIndex, currentIndex + 2 + 2 * tmpInt); // +2 for '8x' and x times 2 for
                                                                                     // the number of the following
                                                                                     // bytes
                currentIndex = currentIndex + 2 + 2 * tmpInt;
            } else {
                currentIndex += 2;
            }
            // tmp = toTest.substring(currentIndex, currentIndex+2*getLengthAsInt(tmp));
            tmpInt = currentIndex + 2 * getLengthAsInt(tmp) - whiteSpaceCount;
            if (toTest.length() < tmpInt) {
                return -1;
            }
            return tmpInt;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Methode überprüft, ob das gegebene TLV Objekt mit Leerzeichen ('00' oder 'FF') beginnt und ermittelt die Anzahl dieser Leerzeichen.
     * 
     * @param tlv
     *            - Das TLV Objekt
     * @return - Die Anzahl der Leerzeichen oder <b>-1</b> wenn keine Leerzeichen gefunden wurden.
     */
    private int getBlankLength(final String tlv) {
        String tmpTlv = tlv;
        if (tmpTlv == null) {
            return -1;
        }
        int index = 0;
        tmpTlv = tmpTlv.toUpperCase();
        while (tmpTlv.startsWith(WHITESPACE_00) || tmpTlv.startsWith("FF")) {
            index += 2;
            tmpTlv = tmpTlv.substring(2);
        }
        if (tmpTlv.length() < 1) {
            return -1;
        }
        if (checkForTLV(tmpTlv) > 0) {
            return index;
        }
        return -1;
    }

    /**
     * Methode überprüft, ob das übergebene Element ein gültiges TLV Objekt ist.
     * 
     * @param tlv
     *            - Das TLV Objekt
     * @return <b>true</b> wenn das TLV Element korrekt aufgebaut ist, sonst <b>false</b>
     */
    public static boolean isValidTlv(final String tlv) {
        try {
            BER_TLV ber = new BER_TLV(tlv);
            String tmp = ber.toString();
            if (tmp.equalsIgnoreCase(tlv)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    // ----------StringTools -------
    private static final int RADIX_BIN_2 = 2;

    /**
     * Methode macht erweitert eine String zu einem String mit gerader Anzahl Characters. Dazu wird dem String eine "0" vorangestellt.
     *
     * @param string
     *            - Ein beliebiger String
     * @return Ein ... String der ... ist, wenn benötigt wird hier eine '0' angehangen um den String ... zu machen.
     */
    public static String makeEven(final String string) {
        return ((string.length() % RADIX_BIN_2 == 0) ? "" : "0") + string;
    }

    public static void main(String[] args) {
        new BER_TLV("800113");
    }
}
