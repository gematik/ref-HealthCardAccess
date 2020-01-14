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
 * The format 2 PIN block has been specified for use with IC cards. The format 2 PIN block shall only be used in
 * an  offline  environment  and  shall  not  be  used  for  online  PIN  verification.  This  PIN  block  is  constructed  by
 * concatenation of two fields: the plain text PIN field and the filler field.
 *
 * image::HCARDA/Format-2-PIN.png[Format-2-PIN,600]
 *
 * @see "ISO 9564-1"
 *
 *
 */
public class Format2Pin implements ICardItem {
    private static final int NIBBLE_SIZE = 4;

    private static final int MIN_PIN_LEN = 4; // specSpec_COS#N008.000
    private static final int MAX_PIN_LEN = 12; // specSpec_COS#N008.000
    private static final int FORMAT_PIN_2_ID = 0x02 << NIBBLE_SIZE; // specSpec_COS#N008.100
    private static final int FORMAT2_PIN_SIZE = 8;
    private static final int FORMAT2_PIN_FILLER = 0x0F;

    private static final int MIN_DIGIT = 0; // specSpec_COS#N008.000
    private static final int MAX_DIGIT = 9; // specSpec_COS#N008.000

    private final int[] pin;
    private final byte[] format2Pin;

    public Format2Pin(final int[] pin) {
        this.pin = pin;
        sanityCheck();

        format2Pin = new byte[FORMAT2_PIN_SIZE]; // specSpec_COS#N008.100
        format2Pin[0] = (byte) (FORMAT_PIN_2_ID + pin.length);

        for (int i = 0; i < pin.length; i++) {

            if (((i + 2) % 2) == 0) {
                format2Pin[1 + i / 2] += (byte) (pin[i] << NIBBLE_SIZE);
            } else {
                format2Pin[1 + i / 2] += (byte) pin[i];
            }
        }

        for (int i = pin.length; i < (2 * FORMAT2_PIN_SIZE - 2); i++) {
            if ((i % 2) == 0) {
                format2Pin[1 + i / 2] += (byte) (FORMAT2_PIN_FILLER << NIBBLE_SIZE);
            } else {
                format2Pin[1 + i / 2] += (byte) FORMAT2_PIN_FILLER;
            }
        }
    }

    public byte[] getFormat2Pin() {
        return format2Pin;
    }

    public void sanityCheck() {
        if (pin.length < MIN_PIN_LEN) {
            throw new IllegalArgumentException("PIN length is too short, min length is " + MIN_PIN_LEN + ", but was " + pin.length);
        }
        if (pin.length > MAX_PIN_LEN) {
            throw new IllegalArgumentException("PIN length is too long, max length is " + MAX_PIN_LEN + ", but was " + pin.length);
        }
        for (int i = 0; i < pin.length; i++) {
            if (pin[i] < MIN_DIGIT || pin[i] > MAX_DIGIT) {
                throw new IllegalArgumentException("PIN digit value is out of range of a decimal digit: " + pin[i]);
            }
        }
    }

}
