/*
 * Copyright (C) 2022 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expediagroup.sdk.core.plugin.authentication.strategy.signature

import com.expediagroup.sdk.core.commons.TestConstants.CLIENT_KEY_TEST_CREDENTIAL
import com.expediagroup.sdk.core.commons.TestConstants.CLIENT_SECRET_TEST_CREDENTIAL
import com.expediagroup.sdk.core.commons.TestConstants.MANUALLY_CALCULATED_SIGNATURE
import com.expediagroup.sdk.core.commons.TestConstants.SIGNATURE_TIMESTAMP_UTC
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SignatureCalculatorTest {
    @Test
    fun `given key, secret, and localDateTime, then calculate signature properly`() {
        val signature = calculateSignature(
            CLIENT_KEY_TEST_CREDENTIAL,
            CLIENT_SECRET_TEST_CREDENTIAL,
            SIGNATURE_TIMESTAMP_UTC
        )

        assertThat(signature).isEqualTo(MANUALLY_CALCULATED_SIGNATURE)
    }
}
