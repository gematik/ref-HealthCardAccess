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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.gematik.ti.healthcardaccess.commands.ExternalMutualAuthenticateCommand;
import de.gematik.ti.healthcardaccess.commands.InternalAuthenticateCommand;
import de.gematik.ti.healthcardaccess.commands.PsoComputeCryptographicChecksum;
import de.gematik.ti.healthcardaccess.commands.PsoDecipher;
import de.gematik.ti.healthcardaccess.commands.PsoEncipher;
import de.gematik.ti.healthcardaccess.commands.PsoVerifyCertificateCommand;
import de.gematik.ti.healthcardaccess.commands.PsoVerifyDigitalSignatureCommand;

/**
 *
 */
public class CmdDataChecker implements ISanityChecker<byte[]> {

    private static ISanityChecker instance;

    private final Map<Object, Object> valuePair = new HashMap<>();
    private Object currentParameter;
    private String errorMessage;
    private final List<Object> list = new ArrayList<>();
    private final boolean checkDataLength = false;
    private Object currentCommandClazz;

    public static ISanityChecker getInstance() {
        if (instance == null) {
            instance = new CmdDataChecker();
        }
        return instance;
    }

    @Override
    public void check(final byte[] cmdData) throws SanityCheckFailedException {
        try {
            if (currentCommandClazz.equals(PsoVerifyDigitalSignatureCommand.class)) {
                CheckPsoVerifyDigitalSignatureImpl.check(errorMessage, cmdData);
            } else if (currentCommandClazz.equals(PsoVerifyCertificateCommand.class)) {
                CheckPsoVerifyCertificateCommandImpl.check(errorMessage, cmdData);
            } else if (currentCommandClazz.equals(PsoEncipher.class)) {
                CheckPsoEncipherImpl.check(errorMessage, cmdData);
            } else if (currentCommandClazz.equals(PsoDecipher.class)) {
                CheckPsoDecipherImpl.check(errorMessage, cmdData);
            } else if (currentCommandClazz.equals(PsoComputeCryptographicChecksum.class)) {
                CheckPsoComputeCryptographicChecksumImpl.check(errorMessage, cmdData);
            } else if (currentCommandClazz.equals(InternalAuthenticateCommand.class)) {
                CheckInternalAuthenticateCommandImpl.check(errorMessage, cmdData);
            } else if (currentCommandClazz.equals(ExternalMutualAuthenticateCommand.class)) {
                CheckExternalMutualAuthenticateCommandImpl.check(errorMessage, cmdData);
            }
        } finally {
            clean();
        }
    }

    private void clean() {
        valuePair.clear();
        errorMessage = "";
        currentParameter = null;
        currentCommandClazz = null;
        list.clear();
    }

    @Override
    public ISanityChecker<byte[]> setSpecifiedValues(final byte[]... values) {
        list.addAll(Arrays.asList(values));
        return instance;
    }

    @Override
    public ISanityChecker setSpecialConfigurationPair(final String key, final int valueSpec) {
        valuePair.put(key, valueSpec);
        return instance;
    }

    @Override
    public ISanityChecker setCurrentParameter(final Object parameter) {
        if (parameter instanceof Class) {
            this.currentCommandClazz = parameter;
        } else {
            this.currentParameter = parameter;
        }
        return instance;
    }

    @Override
    public ISanityChecker<byte[]> setMsgIncaseError(final String errMsgKey) {
        errorMessage = RESOURCE_BUNDLE.getString(errMsgKey);
        return instance;
    }

}
