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

package de.gematik.ti.healthcardaccess;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import de.gematik.ti.cardreader.provider.api.card.ICard;
import de.gematik.ti.healthcardaccess.healthcards.Egk21;
import de.gematik.ti.healthcardaccess.healthcards.HealthCardStatusInvalid;
import de.gematik.ti.healthcardaccess.healthcards.HealthCardStatusUnknown;
import de.gematik.ti.healthcardaccess.healthcards.HealthCardStatusValid;
import de.gematik.ti.healthcardaccess.healthcards.Unknown;

public class HealthCardTest {

    private final ICard card = PowerMockito.mock(ICard.class);

    @Test
    public void setHealthCardType() {
        HealthCard hc = new HealthCard(card);
        Assert.assertEquals(HealthCardStatusUnknown.class, hc.getStatus().getClass());
        hc.setHealthCardType(new Egk21());
        Assert.assertEquals(HealthCardStatusValid.class, hc.getStatus().getClass());
        Assert.assertEquals(Egk21.class, ((HealthCardStatusValid) hc.getStatus()).getHealthCardType().getClass());
        hc.setHealthCardType(new Unknown());
        Assert.assertEquals(HealthCardStatusInvalid.class, hc.getStatus().getClass());
    }
}
