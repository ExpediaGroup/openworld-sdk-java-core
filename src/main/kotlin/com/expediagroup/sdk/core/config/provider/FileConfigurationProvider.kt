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
package com.expediagroup.sdk.core.config.provider

import com.expediagroup.sdk.core.constant.ExceptionMessage.NOT_YET_IMPLEMENTED
import com.expediagroup.sdk.core.constant.provider.ExceptionMessageProvider.getPropertyNotFoundMessage
import com.expediagroup.sdk.core.model.exception.client.OpenWorldConfigurationException
import com.expediagroup.sdk.core.plugin.logging.OpenWorldLoggerFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Properties

/**
 * An implementation of [ConfigurationProvider] that represents a Properties file.
 * All property keys and values are stored as cleartext.
 */
class FileConfigurationProvider : ConfigurationProvider {
    private val emptyConfigurationData = ConfigurationData(emptyMap())

    companion object {
        private val log = OpenWorldLoggerFactory.getLogger(FileConfigurationProvider::class.java)
    }

    /**
     * Retrieves the data at the given Properties file.
     *
     * @param path the file where the data resides
     * @param optional whether these configurations are optional or not
     * @return the configuration data
     */
    override fun get(path: String, optional: Boolean): ConfigurationData {
        if (path.isEmpty()) {
            return emptyConfigurationData
        }

        runCatching {
            return readPropsFileIntoConfigurationData(getReader(path))
        }.getOrElse {
            if (!optional) {
                throw OpenWorldConfigurationException(getPropertyNotFoundMessage(path))
            }

            return emptyConfigurationData
        }
    }

    /**
     * Retrieves the data at the given Properties file.
     *
     * We used it with getting the files within jar file.
     * @param url the file url where the data resides
     * @param optional whether these configurations are optional or not
     * @return the configuration data
     */
    override fun get(url: URL, optional: Boolean): ConfigurationData {
        return runCatching {
            readPropsFileIntoConfigurationData(getReader(url))
        }.getOrElse {
            if (!optional) throw OpenWorldConfigurationException(getPropertyNotFoundMessage(url))

            return emptyConfigurationData
        }
    }

    @Suppress("NotImplementedDeclaration")
    override fun subscribe(path: String, keys: Set<String>, callback: ConfigurationChangeCallback) {
        log.warn(NOT_YET_IMPLEMENTED)
        TODO(NOT_YET_IMPLEMENTED)
    }

    @Suppress("NotImplementedDeclaration")
    override fun unsubscribe(path: String, keys: Set<String>, callback: ConfigurationChangeCallback) {
        log.warn(NOT_YET_IMPLEMENTED)
        TODO(NOT_YET_IMPLEMENTED)
    }

    @Suppress("NotImplementedDeclaration")
    override fun unsubscribeAll() {
        log.warn(NOT_YET_IMPLEMENTED)
        TODO(NOT_YET_IMPLEMENTED)
    }

    /**
     * Retrieves the data with the given keys at the given Properties file.
     *
     * @param path the file where the data resides
     * @param keys the keys whose values will be retrieved
     * @param optional whether these configurations are optional or not
     * @return the configuration data
     */
    override fun get(path: String, keys: Set<String>, optional: Boolean): ConfigurationData {
        val data: MutableMap<String, String> = HashMap()
        if (path.isEmpty()) {
            return emptyConfigurationData
        }
        try {
            getReader(path).use { reader ->
                val properties = Properties()
                properties.load(reader)
                for (key in keys) {
                    val value = properties.getProperty(key)
                    data[key] = value
                }
                return ConfigurationData(data)
            }
        } catch (e: IOException) {
            if (!optional) {
                throw OpenWorldConfigurationException(getPropertyNotFoundMessage(path), e)
            }

            return emptyConfigurationData
        }
    }

    private fun readPropsFileIntoConfigurationData(reader: Reader): ConfigurationData {
        val data: MutableMap<String, String> = HashMap()
        reader.use {
            val properties = Properties()
            properties.load(it)
            val keys = properties.keys()
            while (keys.hasMoreElements()) {
                val key = keys.nextElement().toString()
                val value = properties.getProperty(key)
                data[key] = value
            }
            return ConfigurationData(data)
        }
    }

    @Throws(IOException::class)
    private fun getReader(path: String): Reader = Files.newBufferedReader(Paths.get(path))

    @Throws(IOException::class)
    private fun getReader(url: URL): Reader = BufferedReader(InputStreamReader(url.openStream()))
}
