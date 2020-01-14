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

import java.io.IOException;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cardfilesystem.egk21mf.Sk;
import de.gematik.ti.healthcardaccess.AbstractHealthCardCommand;
import de.gematik.ti.healthcardaccess.cardobjects.GemCvCertificate;
import de.gematik.ti.healthcardaccess.cardobjects.Key;
import de.gematik.ti.healthcardaccess.cardobjects.PsoAlgorithm;
import de.gematik.ti.healthcardaccess.commands.ManageSecurityEnvironmentCommand.MseUseCase;
import de.gematik.ti.healthcardaccess.sanitychecker.SanityCheckFailedException;

/**
 * Test {@link ManageSecurityEnvironmentCommand}
 *
 */
@RunWith(Theories.class)
public class ManageSecurityEnvironmentCommandTest {
    private static final Logger LOG = LoggerFactory.getLogger(ManageSecurityEnvironmentCommandTest.class);
    @DataPoint
    public static boolean[] boolAray = { true, false };

    private final TestHealthCard card = new TestHealthCard();
    private final TestResource testResource = new TestResource();

    /**
     * ManageSecurityEnvironmentCommand(int)
     */
    @Test
    public void shouldEqualManageSecurityEnvironmentCommandSeNumber() {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.MANAGESECURITYENVIRONMENTCOMMAND_APDU, 1);
        final int seNumber = 1;
        final AbstractHealthCardCommand command = new ManageSecurityEnvironmentCommand(seNumber);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * ManageSecurityEnvironmentCommand(de.gematik.ti.healthcardaccess.commands.ManageSecurityEnvironmentCommand$MseUseCase,de.gematik.ti.healthcardaccess.
     * cardobjects.Key,boolean,byte[])
     *
     * @param dfSpecific
     *
     * @throws IOException
     */
    @Theory
    public void shouldEqualManageSecurityEnvironmentCommandMseUseCaseKeyBooleanOid(final boolean dfSpecific) throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.MANAGESECURITYENVIRONMENTCOMMAND_APDU, 3, dfSpecific);
        final MseUseCase mseUseCase = MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITHOUT_CURVES;

        final Key key = TestResource.KEY_PRK_EGK_AUT_CVC_E256;
        // final GemCvCertificate gemCvCertificate = (GemCvCertificate) testResource.getParameter(ParameterEnum.PARAMETER_GEMCVCERTIFICATE);
        final byte[] oid = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_OID);
        final AbstractHealthCardCommand command = new ManageSecurityEnvironmentCommand(mseUseCase, key, dfSpecific, oid);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Theory
    public void shouldEqualManageSecurityEnvironmentCommandMseUseCaseKeyDfSpecificOidIdDomain(final boolean dfSpecific) throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.MANAGESECURITYENVIRONMENTCOMMAND_APDU, 4, dfSpecific);
        final MseUseCase mseUseCase = MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITH_CURVES;
        final Key key = TestResource.KEY_PRK_EGK_AUT_CVC_E256;
        // final GemCvCertificate gemCvCertificate = (GemCvCertificate) testResource.getParameter(ParameterEnum.PARAMETER_GEMCVCERTIFICATE);
        final byte[] oid = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_OID);
        final int idDomain = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_IDDOMAIN);
        final AbstractHealthCardCommand command = new ManageSecurityEnvironmentCommand(mseUseCase, key, dfSpecific, oid, idDomain);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * {@link ManageSecurityEnvironmentCommand#ManageSecurityEnvironmentCommand(MseUseCase, GemCvCertificate)}
     *
     * @throws IOException
     */
    @Test
    public void shouldEqualManageSecurityEnvironmentCommandMseUseCaseGemCvCertificate() throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.MANAGESECURITYENVIRONMENTCOMMAND_APDU, 5);
        final MseUseCase mseUseCase = MseUseCase.KEY_SELECTION_FOR_CV_CERTIFICATE_VALIDATION;
        final GemCvCertificate gemCvCertificate = testResource.getGemCvcCertificate();
        final AbstractHealthCardCommand command = new ManageSecurityEnvironmentCommand(mseUseCase, gemCvCertificate);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    /**
     * ManageSecurityEnvironmentCommand(de.gematik.ti.healthcardaccess.commands.ManageSecurityEnvironmentCommand$MseUseCase,de.gematik.ti.healthcardaccess.
     * cardobjects.PsoAlgorithm,byte[])
     *
     * @throws IOException
     */
    @Test
    public void shouldEqualManageSecurityEnvironmentCommandMseUseCasePsoAlgorithmKeyReference() throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.MANAGESECURITYENVIRONMENTCOMMAND_APDU, 6);
        final MseUseCase mseUseCase = MseUseCase.KEY_SELECTION_FOR_EXTERNAL_ASYMMETRIC_AUTHENTICATION;
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_CLIENT_AUTHENTICATION);
        final byte[] keyReference = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_REFERENCE);
        final AbstractHealthCardCommand command = new ManageSecurityEnvironmentCommand(mseUseCase, psoAlgorithm, keyReference);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Test
    public void shouldEqualManageSecurityEnvironmentCommandMseUseCasePsoAlgorithmKeyReferenceSymmKey() throws IOException {
        final String expectedAPDU = testResource.getExpectApdu(ApduResultEnum.MANAGESECURITYENVIRONMENTCOMMAND_APDU, 7);
        final MseUseCase mseUseCase = MseUseCase.KEY_SELECTION_FOR_INTERNAL_SYMMETRIC_AUTHENTICATION;
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_CLIENT_AUTHENTICATION);
        final Sk.ICMSSymkey cmsSymkey = Sk.CMS_AES256;
        final AbstractHealthCardCommand command = new ManageSecurityEnvironmentCommand(mseUseCase,
                psoAlgorithm, cmsSymkey);
        command.executeOn(card).test();

        Assert.assertThat(expectedAPDU, IsEqual.equalTo(card.getLastCommandAPDUString()));

    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Negative test for testId 4
     *
     * @throws IOException
     */
    @Theory
    public void testSanityCheckNullKeyNegative(boolean dfSpecific) throws IOException {
        thrown.expect(SanityCheckFailedException.class);
        String errMsg = "Provided key argument for selected use case %s must be not null.";
        thrown.expectMessage(errMsg);
        LOG.info(errMsg);
        final MseUseCase mseUseCase = MseUseCase.KEY_SELECTION_FOR_SYMMETRIC_CARD_CONNECTION_WITH_CURVES;
        final Key key = null;
        final byte[] oid = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_OID);
        final int idDomain = (int) testResource.getParameter(ParameterEnum.PARAMETER_INT_IDDOMAIN);
        new ManageSecurityEnvironmentCommand(mseUseCase, key, dfSpecific, oid, idDomain);

    }

    /**
     * Negative test for testId 1
     *
     * @throws IOException
     */
    @Test
    public void testSanityCheckSeNumberNegative() throws IOException {
        thrown.expect(SanityCheckFailedException.class);
        String errMsg = "Argument security environment number 10000 out of range [1,4]";
        thrown.expectMessage(errMsg);
        LOG.info(errMsg);
        final int seNumber = 10000;
        LOG.info("current seNumber: {}", seNumber);
        new ManageSecurityEnvironmentCommand(seNumber);

    }

    /**
     * Negative test for testId 6
     *
     * @throws IOException
     */
    @Test
    public void testSanityCheckMseUseCaseNegative() throws IOException {
        thrown.expect(SanityCheckFailedException.class);
        String errMsg = "For this constructor of ManageSecurityEnvironmentCommand the chosen use case must be from the set [KEY_SELECTION_FOR_EXTERNAL_ASYMMETRIC_AUTHENTICATION, KEY_SELECTION_FOR_DATA_ENCODING].";
        thrown.expectMessage(errMsg);
        LOG.info(errMsg);
        final MseUseCase mseUseCase = MseUseCase.KEY_SELECTION_FOR_CV_CERTIFICATE_VALIDATION;
        LOG.info("current mseUseCase: {}", mseUseCase);
        final PsoAlgorithm psoAlgorithm = new PsoAlgorithm(PsoAlgorithm.Algorithm.AUTHENTICATE_RSA_CLIENT_AUTHENTICATION);
        final byte[] keyReference = (byte[]) testResource.getParameter(ParameterEnum.PARAMETER_BYTEARRAY_REFERENCE);
        new ManageSecurityEnvironmentCommand(mseUseCase, psoAlgorithm, keyReference);

    }

}
