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
package com.expediagroup.sdk.core.client.openapi

import com.expediagroup.sdk.core.client.Client
import com.expediagroup.sdk.core.configuration.ClientConfiguration
import com.expediagroup.sdk.core.constant.Constant
import com.expediagroup.sdk.core.constant.provider.LoggingMessageProvider.getResponseUnsuccessfulMessage
import com.expediagroup.sdk.core.model.error.Error
import com.expediagroup.sdk.core.model.exception.service.OpenWorldServiceException
import com.expediagroup.sdk.core.plugin.logging.OpenWorldLoggerFactory
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.toURI

/**
 * Base class used by generated OpenAPI APIs to perform the heavy lifting of actually sending requests.
 */
@SuppressWarnings("UnnecessaryAbstractClass")
abstract class OpenApiStub(
    private val clientConfiguration: ClientConfiguration = ClientConfiguration.EMPTY
) {
    protected val client: Client by lazy { createClient() }

    companion object {
        private val log = OpenWorldLoggerFactory.getLogger(OpenApiStub::class.java)
    }

    /**
     * Create a new [Client] instance to be used by the generated OpenAPI APIs.
     */
    protected open fun createClient(): Client = Client.from(OkHttp.create(), clientConfiguration)

    protected fun collectionDelimiter(format: String): String {
        return when (format) {
            "csv" -> ","
            "tsv" -> "\t"
            "pipes" -> "|"
            else -> " "
        }
    }

    /**
     * Examines the status code for errors and throws the appropriate exception.
     *
     * @param response The response to examine
     * @throws OpenWorldServiceException if an error response is received from the service.
     */
    protected suspend fun throwIfError(response: HttpResponse) {
        if (isNotSuccessfulResponse(response)) {
            log.info(getResponseUnsuccessfulMessage(response.status))
            throwServiceException(response)
        }
    }

    protected open suspend fun throwServiceException(response: HttpResponse) {
        // Make sure we read the body to avoid resource leaks
        runCatching {
            response.body<Error>()
        }.onSuccess {
            throw OpenWorldServiceException(response.status, it)
        }.onFailure {
            throw OpenWorldServiceException(
                response.status,
                Error(response.request.url.toURI(), response.status.description)
            )
        }
    }

    private fun isNotSuccessfulResponse(response: HttpResponse) = response.status.value !in Constant.SUCCESSFUL_STATUS_CODES_RANGE
}
