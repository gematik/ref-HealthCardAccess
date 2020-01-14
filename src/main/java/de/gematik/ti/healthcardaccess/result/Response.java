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

package de.gematik.ti.healthcardaccess.result;

import java.util.Arrays;
import java.util.EnumSet;

import de.gematik.ti.healthcardaccess.WrongCardDataException;
import de.gematik.ti.healthcardaccess.operation.Result;

/**
 * Command Execution Response object
 *
 * @created 05-Jul-2018 14:33:52
 */
public class Response {

    private final ResponseStatus responseStatus;
    private final byte[] responseData;
//    private final AbstractHealthCardCommand callingCommand;

    /**
     * Create a new Response with ResponseStatus and Data
     * @param responseStatus - Status Code
     * @param responseData - response data
     */
    public Response(final ResponseStatus responseStatus, final byte[] responseData) {
        this.responseStatus = responseStatus;
//        this.callingCommand = abstractHealthCardCommand;
        if (responseData != null) {
            this.responseData = Arrays.copyOf(responseData, responseData.length);
        } else {
            this.responseData = null;
        }
    }

    /**
     *
     * @return the ResponseStatus
     */
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    /**
     *
     * @return the response data array
     */
    public byte[] getResponseData() {
        return responseData;
    }

    /**
     * All response status codes
     * @see "gemSpec_COS_16.2"
     */
    public enum ResponseStatus { // spec: gemSpec_COS_16.2
        SUCCESS, // (0x9000)
        UNKNOWN_EXCEPTION, // (0x6F00)
        UNKNOWN_STATUS, // (0x0000)
        DATA_TRUNCATED, // (0x6200)
        CORRUPT_DATA_WARNING, // (0x6281)
        END_OF_FILE_WARNING, // (0x6282)
        END_OF_RECORD_WARNING, // (0x6282)
        UNSUCCESSFUL_SEARCH, // (0x6282)
        FILE_DEACTIVATED, // (0x6282)
        FILE_TERMINATED, // (0x6283)
        RECORD_DEACTIVATED, // (0x6287)
        TRANSPORT_STATUS_TRANSPORT_PIN, // (0x62C1)
        TRANSPORT_STATUS_EMPTY_PIN, // (0x62C7)
        PASSWORD_DISABLED, // (0x62D0)
        AUTHENTICATION_FAILURE, // (0x6300)
        NO_AUTHENTICATION, // (0x63CF)
        RETRY_COUNTER_COUNT_00, // (0x63C0)
        RETRY_COUNTER_COUNT_01, // (0x63C1)
        RETRY_COUNTER_COUNT_02, // (0x63C2)
        RETRY_COUNTER_COUNT_03, // (0x63C3)
        RETRY_COUNTER_COUNT_04, // (0x63C4)
        RETRY_COUNTER_COUNT_05, // (0x63C5)
        RETRY_COUNTER_COUNT_06, // (0x63C6)
        RETRY_COUNTER_COUNT_07, // (0x63C7)
        RETRY_COUNTER_COUNT_08, // (0x63C8)
        RETRY_COUNTER_COUNT_09, // (0x63C9)
        RETRY_COUNTER_COUNT_10, // (0x63CA)
        RETRY_COUNTER_COUNT_11, // (0x63CB)
        RETRY_COUNTER_COUNT_12, // (0x63CC)
        RETRY_COUNTER_COUNT_13, // (0x63CD)
        RETRY_COUNTER_COUNT_14, // (0x63CE)
        RETRY_COUNTER_COUNT_15, // (0x63CF)
        UPDATE_RETRY_WARNING_COUNT_00, // (0x63C0)
        UPDATE_RETRY_WARNING_COUNT_01, // (0x63C1)
        UPDATE_RETRY_WARNING_COUNT_02, // (0x63C2)
        UPDATE_RETRY_WARNING_COUNT_03, // (0x63C3)
        UPDATE_RETRY_WARNING_COUNT_04, // (0x63C4)
        UPDATE_RETRY_WARNING_COUNT_05, // (0x63C5)
        UPDATE_RETRY_WARNING_COUNT_06, // (0x63C6)
        UPDATE_RETRY_WARNING_COUNT_07, // (0x63C7)
        UPDATE_RETRY_WARNING_COUNT_08, // (0x63C8)
        UPDATE_RETRY_WARNING_COUNT_09, // (0x63C9)
        UPDATE_RETRY_WARNING_COUNT_10, // (0x63CA)
        UPDATE_RETRY_WARNING_COUNT_11, // (0x63CB)
        UPDATE_RETRY_WARNING_COUNT_12, // (0x63CC)
        UPDATE_RETRY_WARNING_COUNT_13, // (0x63CD)
        UPDATE_RETRY_WARNING_COUNT_14, // (0x63CE)
        UPDATE_RETRY_WARNING_COUNT_15, // (0x63CF)
        WRONG_SECRET_WARNING_COUNT_00, // (0x63C0)
        WRONG_SECRET_WARNING_COUNT_01, // (0x63C1)
        WRONG_SECRET_WARNING_COUNT_02, // (0x63C2)
        WRONG_SECRET_WARNING_COUNT_03, // (0x63C3)
        WRONG_SECRET_WARNING_COUNT_04, // (0x63C4)
        WRONG_SECRET_WARNING_COUNT_05, // (0x63C5)
        WRONG_SECRET_WARNING_COUNT_06, // (0x63C6)
        WRONG_SECRET_WARNING_COUNT_07, // (0x63C7)
        WRONG_SECRET_WARNING_COUNT_08, // (0x63C8)
        WRONG_SECRET_WARNING_COUNT_09, // (0x63C9)
        WRONG_SECRET_WARNING_COUNT_10, // (0x63CA)
        WRONG_SECRET_WARNING_COUNT_11, // (0x63CB)
        WRONG_SECRET_WARNING_COUNT_12, // (0x63CC)
        WRONG_SECRET_WARNING_COUNT_13, // (0x63CD)
        WRONG_SECRET_WARNING_COUNT_14, // (0x63CE)
        WRONG_SECRET_WARNING_COUNT_15, // (0x63CF)
        ENCIPHER_ERROR, // (0x6400)
        KEY_INVALID, // (0x6400)
        OBJECT_TERMINATED, // (0x6400)
        PARAMETER_MISMATCH, // (0x6400)
        MEMORY_FAILURE, // (0x6581)
        WRONG_RECORD_LENGTH, // (0x6700)
        CHANNEL_CLOSED, // (0x6881)
        NO_MORE_CHANNELS_AVAILABLE, // (0x6981)
        VOLATILE_KEY_WITHOUT_LCS, // (0x6981)
        WRONG_FILE_TYPE, // (0x6981)
        SECURITY_STATUS_NOT_SATISFIED, // (0x6982)
        COMMAND_BLOCKED, // (0x6983)
        KEY_EXPIRED, // (0x6983)
        PASSWORD_BLOCKED, // (0x6983)
        KEY_ALREADY_PRESENT, // (0x6985)
        NO_KEY_REFERENCE, // (0x6985)
        NO_PRK_REFERENCE, // (0x6985)
        NO_PUK_REFERENCE, // (0x6985)
        NO_RANDOM, // (0x6985)
        NO_RECORD_LIFE_CYCLE_STATUS, // (0x6985)
        PASSWORD_NOT_USABLE, // (0x6985)
        WRONG_RANDOM_LENGTH, // (0x6985)
        WRONG_RANDOM_OR_NO_KEY_REFERENCE, // (0x6985)
        WRONG_PASSWORD_LENGTH, // (0x6985)
        NO_CURRENT_EF, // (0x6986)
        INCORRECT_SM_DO, // (0x6988)
        NEW_FILE_SIZE_WRONG, // (0x6A80)
        NUMBER_PRECONDITION_WRONG, // (0x6A80)
        NUMBER_SCENARIO_WRONG, // (0x6A80)
        VERIFICATION_ERROR, // (0x6A80)
        WRONG_CIPHER_TEXT, // (0x6A80)
        WRONG_TOKEN, // (0x6A80)
        UNSUPPORTED_FUNCTION, // (0x6A81)
        FILE_NOT_FOUND, // (0x6A82)
        RECORD_NOT_FOUND, // (0x6A83)
        DATA_TOO_BIG, // (0x6A84)
        FULL_RECORD_LIST, // (0x6A84)
        MESSAGE_TOO_LONG, // (0x6A84)
        OUT_OF_MEMORY, // (0x6A84)
        INCONSISTENT_KEY_REFERENCE, // (0x6A88)
        WRONG_KEY_REFERENCE, // (0x6A88)
        KEY_NOT_FOUND, // (0x6A88)
        KEY_OR_PRK_NOT_FOUND, // (0x6A88)
        PASSWORD_NOT_FOUND, // (0x6A88)
        PRK_NOT_FOUND, // (0x6A88)
        PUK_NOT_FOUND, // (0x6A88)
        DUPLICATED_OBJECTS, // (0x6A89)
        DF_NAME_EXISTS, // (0x6A8A)
        OFFSET_TOO_BIG, // (0x6B00)
        INSTRUCTION_NOT_SUPPORTED; //(0x6D00)

        /**
         * Check if the status code is valid
         * @param response -  response to check
         * @return true if valid otherwise false
         */
        public boolean validate(final Response response) {
            return this == response.getResponseStatus();
        }

        /**
         * Create a Result object for the given Response
         * @param response - Response to create the Result Object
         * @return if valid Success Result Object otherwise Failure
         *
         * @see Result
         * @see de.gematik.ti.healthcardaccess.operation.Result.Success
         * @see de.gematik.ti.healthcardaccess.operation.Result.Failure
         */
        public Result<Response> validateResult(final Response response) {
            if (validate(response)) {
                return Result.success(response);
            } else {
                return Result.failure(new WrongCardDataException(
                        String.format("expected status: %s, but was: %s", this, response.getResponseStatus())));
            }
        }
    }

    /**
     * Represents a Status Code Set for a specific Response. This could be a range of status codes
     */
    public enum ResponseStatusSet {
        PROCESS_COMPLETED_WITH_STATUS_9000(EnumSet.of(ResponseStatus.SUCCESS), "9000"),
        PROCESS_COMPLETED(add9000ToSet(EnumSet.range(ResponseStatus.DATA_TRUNCATED, ResponseStatus.WRONG_SECRET_WARNING_COUNT_15)), "61xx-63xx + 9000"),
        PROCESS_COMPLETED_NORMAL(EnumSet.of(ResponseStatus.SUCCESS), "61xx + 9000"),
        PROCESS_COMPLETED_WITH_WARNING(EnumSet.range(ResponseStatus.DATA_TRUNCATED, ResponseStatus.WRONG_SECRET_WARNING_COUNT_15), "62xx-63xx"),
        PROCESS_COMPLETED_WITH_WARNING_NO_DATA_MODIFIED(EnumSet.range(ResponseStatus.DATA_TRUNCATED, ResponseStatus.PASSWORD_DISABLED), "62xx"),
        PROCESS_COMPLETED_WITH_WARNING_DATA_MODIFIED(EnumSet.range(ResponseStatus.AUTHENTICATION_FAILURE, ResponseStatus.WRONG_SECRET_WARNING_COUNT_15),
                "63xx"),
        PROCESS_ABORTED(EnumSet.range(ResponseStatus.ENCIPHER_ERROR, ResponseStatus.INSTRUCTION_NOT_SUPPORTED), "64xx-6Fxx"),
        PROCESS_ABORTED_WITH_EXECUTION_ERROR(EnumSet.range(ResponseStatus.ENCIPHER_ERROR, ResponseStatus.MEMORY_FAILURE), "64xx-65xx"),
        PROCESS_ABORTED_WITH_EXECUTION_ERROR_DATA_MODIFIED(EnumSet.of(ResponseStatus.MEMORY_FAILURE), "65xx"),
        PROCESS_ABORTED_WITH_EXECUTION_ERROR_NO_DATA_MODIFIED(EnumSet.range(ResponseStatus.ENCIPHER_ERROR, ResponseStatus.PARAMETER_MISMATCH), "64xx"),
        PROCESS_ABORTED_WITH_VERIFICATION_ERROR(EnumSet.range(ResponseStatus.WRONG_RECORD_LENGTH, ResponseStatus.INSTRUCTION_NOT_SUPPORTED), "67xx-6Fxx"),

        // summarizing status for ambiguous response codes
        PROCESS_COMPLETED_6282_FEWER_BYTES_THAN_SPECIFIED_OR_FILE_NOT_FOUND(EnumSet.range(ResponseStatus.END_OF_FILE_WARNING, ResponseStatus.FILE_DEACTIVATED),
                "0x6282"),
        PROCESS_COMPLETED_63CX_COUNTER_MODIFIED(EnumSet.range(ResponseStatus.RETRY_COUNTER_COUNT_00, ResponseStatus.WRONG_SECRET_WARNING_COUNT_15), "63Cx"),
        PROCESS_ABORTED_6981_COMMAND_INCOMPATIBLE_TO_FILE_STRUCTURE(EnumSet.range(ResponseStatus.CHANNEL_CLOSED, ResponseStatus.WRONG_FILE_TYPE), "0x6981"),
        PROCESS_ABORTED_6983_ERROR_AUTHENTICATION_MODE_BLOCKED(EnumSet.range(ResponseStatus.COMMAND_BLOCKED, ResponseStatus.PASSWORD_BLOCKED), "0x6983"),
        PROCESS_ABORTED_6985_USAGE_CONDITIONS_NOT_SATISFIED(EnumSet.range(ResponseStatus.KEY_ALREADY_PRESENT, ResponseStatus.WRONG_PASSWORD_LENGTH), "0x6985"),
        PROCESS_ABORTED_6A80_PARAMETERS_IN_DATA_PORTION_INCORRECT(EnumSet.range(ResponseStatus.NEW_FILE_SIZE_WRONG, ResponseStatus.WRONG_TOKEN), "0x6A80"),
        PROCESS_ABORTED_6A84_INSUFFICIENT_MEMORY(EnumSet.range(ResponseStatus.DATA_TOO_BIG, ResponseStatus.OUT_OF_MEMORY), "0x6A84"),
        PROCESS_ABORTED_6A88_REFERENCED_DATA_NOT_FOUND(EnumSet.range(ResponseStatus.INCONSISTENT_KEY_REFERENCE, ResponseStatus.PUK_NOT_FOUND), "0x6A88");

        private final EnumSet<ResponseStatus> set;
        private final String range;

        ResponseStatusSet(final EnumSet<ResponseStatus> set, final String range) {
            this.set = set;
            this.range = range;
        }

        public EnumSet<ResponseStatus> getSet() {
            return this.set;
        }

        public String getRange() {
            return this.range;
        }

        public boolean containsResponseStatus(final ResponseStatus responseStatus) {
            return this.getSet().contains(responseStatus);
        }

        private static EnumSet<ResponseStatus> add9000ToSet(final EnumSet<ResponseStatus> responseStatuses) {
            EnumSet<ResponseStatus> result = EnumSet.copyOf(responseStatuses);
            result.add(ResponseStatus.SUCCESS);
            return result;
        }

        public boolean validate(final Response response) {
            return this.containsResponseStatus(response.getResponseStatus());
        }

        public Result<Response> validateResult(final Response response) {
            if (this.containsResponseStatus(response.getResponseStatus())) {
                return Result.success(response);
            } else {
                return Result.failure(new WrongCardDataException(
                        String.format("expected status: %s, but was: %s", this.getRange(), response.getResponseStatus())));
            }
        }
    }
}
