package org.alitheiavison.devdevil.everything.parser

import java.lang.IllegalArgumentException

data class Spec(
        val sizeSpec: Int,
        val typeSpec: Type,
        val name: String = "Unknown",
        val desc: String = " Unknown"
) : Value {
    override fun getSize(): Int = sizeSpec

    override fun getType(): Type = typeSpec

    override fun toHexaString(): String = throw IllegalArgumentException()
}

data class Definition(val fieldSpec: Array<Spec>, val name: String = "Unknown")