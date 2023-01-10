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
package com.expediagroup.sdk.core.client

import com.expediagroup.sdk.core.configuration.ClientConfiguration
import com.expediagroup.sdk.core.configuration.Credentials
import com.expediagroup.sdk.core.configuration.collector.ConfigurationCollector
import com.expediagroup.sdk.core.configuration.provider.DefaultConfigurationProvider
import com.expediagroup.sdk.core.configuration.provider.FileSystemConfigurationProvider
import com.expediagroup.sdk.core.configuration.toProvider
import com.expediagroup.sdk.core.plugin.Hooks
import com.expediagroup.sdk.core.plugin.authentication.AuthenticationConfiguration
import com.expediagroup.sdk.core.plugin.authentication.AuthenticationHook
import com.expediagroup.sdk.core.plugin.authentication.AuthenticationPlugin
import com.expediagroup.sdk.core.plugin.encoding.EncodingConfiguration
import com.expediagroup.sdk.core.plugin.encoding.EncodingPlugin
import com.expediagroup.sdk.core.plugin.hooks
import com.expediagroup.sdk.core.plugin.logging.LoggingConfiguration
import com.expediagroup.sdk.core.plugin.logging.LoggingPlugin
import com.expediagroup.sdk.core.plugin.plugins
import com.expediagroup.sdk.core.plugin.request.DefaultRequestConfiguration
import com.expediagroup.sdk.core.plugin.request.DefaultRequestPlugin
import com.expediagroup.sdk.core.plugin.serialization.SerializationConfiguration
import com.expediagroup.sdk.core.plugin.serialization.SerializationPlugin
import com.expediagroup.sdk.core.plugin.use
import com.expediagroup.sdk.core.plugin.with
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

/**
 * The integration point between the SDK Core and the product SDKs.
 *
 * @param httpClientEngine The HTTP client engine to use.
 * @param clientConfiguration The configuration for the client.
 */
class Client private constructor(
    httpClientEngine: HttpClientEngine,
    clientConfiguration: ClientConfiguration
) {
    /**
     * The HTTP client to perform requests with.
     */
    val httpClient: HttpClient

    private val configurationCollector: ConfigurationCollector = ConfigurationCollector.create(
        clientConfiguration.toProvider(),
        FileSystemConfigurationProvider(),
        DefaultConfigurationProvider
    )

    init {
        httpClient = HttpClient(httpClientEngine) {
            val authenticationConfiguration = AuthenticationConfiguration.from(
                this,
                Credentials.from(configurationCollector.key, configurationCollector.secret),
                configurationCollector.authEndpoint
            )

            plugins {
                use(LoggingPlugin).with(LoggingConfiguration.from(this))
                use(SerializationPlugin).with(SerializationConfiguration.from(this))
                use(AuthenticationPlugin).with(authenticationConfiguration)
                use(DefaultRequestPlugin).with(DefaultRequestConfiguration.from(this, configurationCollector.endpoint))
                use(EncodingPlugin).with(EncodingConfiguration.from(this))
            }

            hooks {
                use(AuthenticationHook.with(authenticationConfiguration))
            }
        }

        finalize()
    }

    companion object {
        /**
         * Create a Client.
         *
         * @param httpClientEngine The HttpClientEngine to use.
         * @param clientConfiguration The ClientConfiguration to use.
         * @return A Client.
         */
        @JvmOverloads
        fun from(
            httpClientEngine: HttpClientEngine,
            clientConfiguration: ClientConfiguration = ClientConfiguration.EMPTY
        ): Client = Client(httpClientEngine, clientConfiguration)
    }
}

private fun Client.finalize() = Hooks.execute(this)
