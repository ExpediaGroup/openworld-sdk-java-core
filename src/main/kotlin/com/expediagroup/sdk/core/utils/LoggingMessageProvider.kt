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
package com.expediagroup.sdk.core.utils

import io.ktor.http.HttpStatusCode

object LoggingMessageProvider {
    fun tokenExpiresIn(expiresIn: Int) = "New token expires in $expiresIn seconds"
    fun responseStatusCode(statusCode: HttpStatusCode) = ":Response status code [$statusCode]"
    fun responseUnsuccessful(httpStatusCode: HttpStatusCode) = "Unsuccessful response [$httpStatusCode]"
}
