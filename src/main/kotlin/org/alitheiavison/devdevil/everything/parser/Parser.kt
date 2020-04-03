package org.alitheiavison.devdevil.everything.parser

class Parser(val spec: Definition) {
    @ExperimentalUnsignedTypes
    fun parse(buffer: HexaBuffer): Parsed? {
        val fields = mutableListOf<Field>()

        spec.fieldSpec.forEach {
            fields.add(Field(it.name, buffer.next(it.getSize(), it.getType())))
        }
        return Parsed(fields.toTypedArray())
    }
}