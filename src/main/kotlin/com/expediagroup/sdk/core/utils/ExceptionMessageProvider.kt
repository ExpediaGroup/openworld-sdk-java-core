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

internal object ExceptionMessageProvider {
    fun configurationDefinedTwice(configurationName: String) =
        "Configuration %s is defined twice".format(configurationName)

    fun propertyNotFound(path: Any) = "Could not read properties from file [%s]".format(path)

    fun requiredConfigurationsNotDefined(configurations: String) =
        "Some required configurations are not defined: %s".format(configurations)

    fun expectedActualNameValue(expected: String, actual: String, name: String, value: Any) =
        "Expected value to be a %s, but it was a %s, name: %s, value: %s".format(expected, actual, name, value)

    fun expectedNameValue(expected: String, name: String, value: Any) =
        "Expected value to be a %s, name: %s, value: %s".format(expected, name, value)

    fun configurationKeyNotDefined(name: String) = "Configuration key not defined, name: %s".format(name)

    fun configurationUnknown(key: String) = "Unknown configuration %s".format(key)
}
