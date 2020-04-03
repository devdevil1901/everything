package org.alitheiavison.devdevil.everything.parser

data class Field(val name: String, val value: Value) {
    @ExperimentalUnsignedTypes
    val valueString: String =
            when (value) {
                is StringValue -> "${value.value}"
                is SignedByte -> "${value.value}"
                is UnsignedByte -> "${value.value}"
                is SignedWord -> "${value.value}"
                is UnsignedWord -> "${value.value}"
                is SignedDword -> "${value.value}"
                is UnsignedDword -> "${value.value}"
                is SignedQword -> "${value.value}"
                is UnsignedQword -> "${value.value}"
                is FlagOfByte -> "${value.toBinaryString()}"
                is FlagOfWord -> "${value.toBinaryString()}"
                is FlagOfDword -> "${value.toBinaryString()}"
                is FlagOfQword -> "${value.toBinaryString()}"
                else -> "Failed"
            }

    @ExperimentalUnsignedTypes
    override fun toString(): String = "$name: $valueString"
}

@ExperimentalUnsignedTypes
data class Parsed(val fields: Array<Field>) {
    fun getFiled(fieldName: String): Field? = fields.find {
        it.name == fieldName
    }
    override fun toString(): String {
        var log = "[REPORT]\n"
        fields.forEach {
            log += it.toString()
            log += "\n"
        }
        return log
    }
}