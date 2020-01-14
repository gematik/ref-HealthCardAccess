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

public enum ApduResultEnum {
    ACTIVATECOMMAND_APDU,
    ACTIVATERECORDCOMMAND_APDU,
    WRITECOMMAND_APDU,
    VERITYCOMMAND_APDU,
    TERMINATEDFCOMMAND_APDU,
    TERMINATECOMMAND_APDU,
    TERMINATECARDUSAGECOMMAND_APDU,
    SETLOGICALEOFCOMMAND_APDU,
    SEARCHRECORDCOMMAND_APDU,
    READRECORDCOMMAND_APDU,
    READCOMMAND_APDU,
    PSOVERIFYDIGITALSIGNATURECOMMAND_APDU,
    PSOVERIFYCERTIFICATECOMMAND_APDU,
    PSOTRANSCIPHER_APDU,
    PSOENCIPHER_APDU,
    PSODECIPHER_APDU,
    PSOCOMPUTEDIGITALSIGNATURECOMMAND_APDU,
    PSOCOMPUTECRYPTOGRAPHICCHECKSUM_APDU,
    PSOVERIFYCRYPTPGRAPHICCHECKSUMCOMMAND_APDU,
    MANAGESECURITYENVIRONMENTCOMMAND_APDU,
    MANAGECHANNELCOMMAND_APDU,
    LOADAPPLICATIONCOMMAND_APDU,
    LISTPUBLICKEYCOMMAND_APDU,
    INTERNALAUTHENTICATECOMMAND_APDU,
    GETRANDOMCOMMAND_APDU,
    GETPINSTATUSCOMMAND_APDU,
    GETCHALLENGECOMMAND_APDU,
    GENERATEASYMMETRICKEYPAIRCOMMAND_APDU,
    GENERALAUTHENTICATECOMMAND_APDU,
    FINGERPRINTCOMMAND_APDU,
    EXTERNALMUTUALAUTHENTICATECOMMAND_APDU,
    ERASERECORDCOMMAND_APDU,
    ERASECOMMAND_APDU,
    ENABLEVERIFICATIONREQUIREMENTCOMMAND_APDU,
    DISABLEVERIFICATIONREQUIREMENTCOMMAND_APDU,
    DELETERECORDCOMMAND_APDU,
    DELETECOMMAND_APDU,
    DEACTIVATERECORDCOMMAND_APDU,
    DEACTIVATECOMMAND_APDU,
    CHANGEREFERENCEDATACOMMAND_APDU,
    APPENDRECORDCOMMAND_APDU,
    SELECTCOMMAND_APDU,
    UPDATERECORDCOMMAND_APDU,
    UPDATECOMMAND_APDU

}
