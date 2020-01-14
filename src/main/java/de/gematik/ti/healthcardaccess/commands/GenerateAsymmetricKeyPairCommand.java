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

package de.gematik.ti.healthcardaccess.commands;

import java.util.HashMap;
import java.util.Map;

import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.Key;
import de.gematik.ti.healthcardaccess.result.Response;
import de.gematik.ti.healthcardaccess.sanitychecker.EnumsValidationChecker;
import de.gematik.ti.healthcardaccess.sanitychecker.ISanityChecker;

/**
 * Implements gemSpec_COS#14.9.3 Generate Asymmetric Key Pair
 *
 * [plantuml, HCARDA/generated/command/GenerateAsymmetricKeyPairCommand, png, caption="{classdia-caption} {counter:class}: ", 650]
 * ----
 * include::{plantumljavadocdir}/HCARDA/command/GenerateAsymmetricKeyPairCommand.plantuml[]
 * ----
 *
 */
public class GenerateAsymmetricKeyPairCommand extends AbstractHealthCardCommand {
    private static final int CLA = 0x00;
    private static final int INS = 0x46;

    private static final int MODE_GENERATE_IF_KEY_NOT_EXISTS_WITHOUT_OUTPUT_P1 = 0x84;
    private static final int MODE_OVERWRITE_IF_KEY_EXISTS_WITHOUT_OUTPUT_P1 = 0xC4;
    private static final int MODE_READ_PUBLIC_KEY_P1 = 0x81;
    private static final int MODE_GENERATE_IF_KEY_NOT_EXISTS_WITH_OUTPUT_P1 = 0x80;
    private static final int MODE_OVERWRITE_IF_KEY_EXISTS_WITH_OUTPUT_P1 = 0xC0;

    private static final int MODE_AFFECTED_OBJECT_VIA_KEY_REFERENCE_LIST_P2 = 0x00;

    private static final Map<Integer, Response.ResponseStatus> RESPONSE_MESSAGES = new HashMap<>();

    private final GakpUseCase gakpUseCase;

    // gemSpec_COS#14.9.3.1-14.9.3.10
    public enum GakpUseCase {
        GEN_KEY_WO_OVERWRITE_WO_REFERENCE_WO_OUTPUT,
        GEN_KEY_WO_OVERWRITE_W_REFERENCE_WO_OUTPUT,
        GEN_KEY_W_OVERWRITE_WO_REFERENCE_WO_OUTPUT,
        GEN_KEY_W_OVERWRITE_W_REFERENCE_WO_OUTPUT,
        READ_EXISTING_KEY_WO_REFERENCE,
        READ_EXISTING_KEY_W_REFERENCE,
        GEN_KEY_WO_OVERWRITE_WO_REFERENCE_W_OUTPUT,
        GEN_KEY_WO_OVERWRITE_W_REFERENCE_W_OUTPUT,
        GEN_KEY_W_OVERWRITE_WO_REFERENCE_W_OUTPUT,
        GEN_KEY_W_OVERWRITE_W_REFERENCE_W_OUTPUT
    }

    static {
        RESPONSE_MESSAGES.put(0x9000, Response.ResponseStatus.SUCCESS);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C0, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_00);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C1, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_01);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C2, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_02);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x63C3, Response.ResponseStatus.UPDATE_RETRY_WARNING_COUNT_03);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6400, Response.ResponseStatus.KEY_INVALID);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6981, Response.ResponseStatus.MEMORY_FAILURE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6982, Response.ResponseStatus.SECURITY_STATUS_NOT_SATISFIED);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6985, Response.ResponseStatus.NO_KEY_REFERENCE);//NOCS(LAC): Response Code
        RESPONSE_MESSAGES.put(0x6A88, Response.ResponseStatus.KEY_NOT_FOUND);//NOCS(LAC): Response Code
    }


    /**
     * Implements UseCases gemSpec_COS#4.9.3.1-14.9.3.10. An appropriate key object for the selected use case has to be provided.
     */
    public GenerateAsymmetricKeyPairCommand(final GenerateAsymmetricKeyPairCommand.GakpUseCase gakpUseCase, final Key key, final boolean dfSpecific) {
        super(CLA, INS);
        this.gakpUseCase = gakpUseCase;

        ISanityChecker<Enum> checker = EnumsValidationChecker.getInstance();
        checker.setMsgIncaseError("GenerateAsymmetricKeyPairCommand.errMsg").setSpecifiedValues(GakpUseCase.values()).check(gakpUseCase);

        if (GakpUseCase.GEN_KEY_WO_OVERWRITE_WO_REFERENCE_WO_OUTPUT == gakpUseCase) {
            // gemSpec_COS#4.9.3.1
            this.p1 = MODE_GENERATE_IF_KEY_NOT_EXISTS_WITHOUT_OUTPUT_P1;
            this.p2 = MODE_AFFECTED_OBJECT_VIA_KEY_REFERENCE_LIST_P2;
        } else if (GakpUseCase.GEN_KEY_WO_OVERWRITE_W_REFERENCE_WO_OUTPUT == gakpUseCase) {
            // gemSpec_COS#4.9.3.2
            this.p1 = MODE_GENERATE_IF_KEY_NOT_EXISTS_WITHOUT_OUTPUT_P1;
            this.p2 = key.calculateKeyReference(dfSpecific);
        } else if (GakpUseCase.GEN_KEY_W_OVERWRITE_WO_REFERENCE_WO_OUTPUT == gakpUseCase) {
            // gemSpec_COS#4.9.3.3
            this.p1 = MODE_OVERWRITE_IF_KEY_EXISTS_WITHOUT_OUTPUT_P1;
            this.p2 = MODE_AFFECTED_OBJECT_VIA_KEY_REFERENCE_LIST_P2;
        } else if (GakpUseCase.GEN_KEY_W_OVERWRITE_W_REFERENCE_WO_OUTPUT == gakpUseCase) {
            // gemSpec_COS#4.9.3.4
            this.p1 = MODE_OVERWRITE_IF_KEY_EXISTS_WITHOUT_OUTPUT_P1;
            this.p2 = key.calculateKeyReference(dfSpecific);
        } else if (GakpUseCase.READ_EXISTING_KEY_WO_REFERENCE == gakpUseCase) {
            // gemSpec_COS#4.9.3.5
            this.p1 = MODE_READ_PUBLIC_KEY_P1;
            this.p2 = MODE_AFFECTED_OBJECT_VIA_KEY_REFERENCE_LIST_P2;
            this.ne = EXPECT_ALL_WILDCARD;
        } else if (GakpUseCase.READ_EXISTING_KEY_W_REFERENCE == gakpUseCase) {
            // gemSpec_COS#4.9.3.6
            this.p1 = MODE_READ_PUBLIC_KEY_P1;
            this.p2 = key.calculateKeyReference(dfSpecific);
            this.ne = EXPECT_ALL_WILDCARD;
        } else if (GakpUseCase.GEN_KEY_WO_OVERWRITE_WO_REFERENCE_W_OUTPUT == gakpUseCase) {
            // gemSpec_COS#4.9.3.7
            this.p1 = MODE_GENERATE_IF_KEY_NOT_EXISTS_WITH_OUTPUT_P1;
            this.p2 = MODE_AFFECTED_OBJECT_VIA_KEY_REFERENCE_LIST_P2;
            this.ne = EXPECT_ALL_WILDCARD;
        } else if (GakpUseCase.GEN_KEY_WO_OVERWRITE_W_REFERENCE_W_OUTPUT == gakpUseCase) {
            // gemSpec_COS#4.9.3.8
            this.p1 = MODE_GENERATE_IF_KEY_NOT_EXISTS_WITH_OUTPUT_P1;
            this.p2 = key.calculateKeyReference(dfSpecific);
            this.ne = EXPECT_ALL_WILDCARD;
        } else if (GakpUseCase.GEN_KEY_W_OVERWRITE_WO_REFERENCE_W_OUTPUT == gakpUseCase) {
            // gemSpec_COS#4.9.3.9
            this.p1 = MODE_OVERWRITE_IF_KEY_EXISTS_WITH_OUTPUT_P1;
            this.p2 = MODE_AFFECTED_OBJECT_VIA_KEY_REFERENCE_LIST_P2;
            this.ne = EXPECT_ALL_WILDCARD;
        } else if (GakpUseCase.GEN_KEY_W_OVERWRITE_W_REFERENCE_W_OUTPUT == gakpUseCase) {
            // gemSpec_COS#4.9.3.10
            this.p1 = MODE_OVERWRITE_IF_KEY_EXISTS_WITH_OUTPUT_P1;
            this.p2 = key.calculateKeyReference(dfSpecific);
            this.ne = EXPECT_ALL_WILDCARD;
        }
    }

    @Override
    public Map<Integer, Response.ResponseStatus> getStatusResponseMessages() {
        return RESPONSE_MESSAGES;
    }
}
