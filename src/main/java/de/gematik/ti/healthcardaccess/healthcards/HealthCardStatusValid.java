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

package de.gematik.ti.healthcardaccess.healthcards;

import de.gematik.ti.healthcardaccess.IHealthCardStatus;
import de.gematik.ti.healthcardaccess.IHealthCardType;

/**
 * When card type has been identified by this library. This Object contains the identified card type
 *
 */
public class HealthCardStatusValid implements IHealthCardStatus {
    private IHealthCardType healthCardType;

    /**
     * Construct a valid status object with the given card type
     * @param healthCardType - Type of the identified card
     */
    public HealthCardStatusValid(final IHealthCardType healthCardType) {
        this.healthCardType = healthCardType;
    }

    /**
     * Type of the identified card
     * @return object of IHealthCardType
     */
    public IHealthCardType getHealthCardType() {
        return healthCardType;
    }

    /**
     * set the type of the identified card
     * @TODO: needed? Only if card changed at runtime.
     * @param healthCardType - card type
     */
    public void setHealthCardType(final IHealthCardType healthCardType) {
        this.healthCardType = healthCardType;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
