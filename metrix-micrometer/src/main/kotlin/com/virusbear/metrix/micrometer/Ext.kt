package com.virusbear.metrix.micrometer

import com.virusbear.metrix.Identifier
import io.micrometer.core.instrument.Tag

fun Map<String, String>.toTags(): List<Tag> =
    map { (k , v) -> Tag.of(k, v) }

fun Identifier.toMicrometer(): String =
    "$namespace.$path"