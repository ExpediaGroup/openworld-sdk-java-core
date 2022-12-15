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
package com.expediagroup.sdk.core.constant

import java.io.File

internal object Constant {
    const val EMPTY_STRING = ""

    const val BEARER = "Bearer"

    const val KEY = "key"

    const val SECRET = "secret"

    const val ENDPOINT = "endpoint"

    const val AUTH_ENDPOINT = "auth endpoint"

    const val DEFAULT = "default"

    const val FILE_SYSTEM = "file system"

    const val RUNTIME_CONFIGURATION = "runtime configuration"

    private const val SUCCESSFUL_STATUS_CODES_RANGE_START = 200
    private const val SUCCESSFUL_STATUS_CODES_RANGE_END = 299
    val SUCCESSFUL_STATUS_CODES_RANGE: IntRange = SUCCESSFUL_STATUS_CODES_RANGE_START..SUCCESSFUL_STATUS_CODES_RANGE_END

    private val OPEN_WORLD_HOME_PATH = "${System.getProperty("user.home")}${File.separator}.openworld${File.separator}"

    val CREDENTIALS_FILE_PATH = "${OPEN_WORLD_HOME_PATH}credentials"

    val CLIENT_CONFIGS_FILE_PATH = "${OPEN_WORLD_HOME_PATH}configuration"
}
