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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.gematik.ti.utils.codec.Hex;

public class Format2PinTest {

    private static final int[] SHORT_PIN = new int[] { 1, 2, 3 };
    private static final int[] LONG_PIN = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3 };
    private static final int[] OUT_OF_RANGE_PIN = new int[] { 1, 22, 33, 44, 55, 66 };
    private static final int[] VALID_PIN = new int[] { 1, 2, 3, 4, 5, 6 };
    private static final String FORMAT2_VALID_PIN_HEX_STRING = "26123456ffffffff";
    private static final int[] VALID_PIN_ODD = new int[] { 1, 2, 3, 4, 5, 6, 7 };
    private static final String FORMAT2_VALID_PIN_ODD_HEX_STRING = "271234567fffffff";

    @Rule
    public ExpectedException exceptions = ExpectedException.none();

    @Test
    public void shouldBeValidPin() {
        Format2Pin pin = new Format2Pin(VALID_PIN);
        Assert.assertThat(pin, is(IsNull.notNullValue()));
        Assert.assertThat(Hex.encodeHexString(pin.getFormat2Pin(), false), equalToIgnoringCase(FORMAT2_VALID_PIN_HEX_STRING));
    }

    @Test
    public void shouldBeValidPinOdd() {
        Format2Pin pin = new Format2Pin(VALID_PIN_ODD);
        Assert.assertThat(pin, is(IsNull.notNullValue()));
        Assert.assertThat(Hex.encodeHexString(pin.getFormat2Pin(), false), equalToIgnoringCase(FORMAT2_VALID_PIN_ODD_HEX_STRING));
    }

    @Test
    public void shouldFailCauseTooShortPin() {
        exceptions.expect(IllegalArgumentException.class);
        new Format2Pin(SHORT_PIN);
    }

    @Test
    public void shouldFailCauseTooLongPin() {
        exceptions.expect(IllegalArgumentException.class);
        new Format2Pin(LONG_PIN);
    }

    @Test
    public void shouldFailCauseOutOfRangePin() {
        exceptions.expect(IllegalArgumentException.class);
        new Format2Pin(OUT_OF_RANGE_PIN);
    }

}
