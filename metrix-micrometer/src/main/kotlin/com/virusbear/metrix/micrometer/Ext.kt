/*
 * Copyright 2023 Virusbear
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

package com.virusbear.metrix.micrometer

import com.virusbear.metrix.Identifier
import com.virusbear.metrix.Tags
import io.micrometer.core.instrument.Tag

internal fun Tags.toTags(): List<Tag> =
    map { (k , v) -> Tag.of(k, v) }

internal fun Identifier.toMicrometer(): String =
    "$namespace.$path"