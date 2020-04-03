package org.alitheiavison.devdevil.everything.parser

import java.nio.ByteBuffer
import java.nio.ByteOrder

@ExperimentalUnsignedTypes
class HexaBuffer(val bytes: UByteArray) {
    var offset: Int = 0

    fun next(size: Int, type: Type): Value = when (type) {
        Type.UUID -> extractUUID()
        else -> extract(size, type)
    }.also { offset += size }

    fun extractUUID(): StringValue {
        var pop = bytes.copyOfRange(offset, offset + 4)
        val part1 = toHexaString(pop, littleEndian = true) + "-"
        pop = bytes.copyOfRange(offset + 4, offset + 6)
        val part2 = toHexaString(pop, littleEndian = true) + "-"
        pop = bytes.copyOfRange(offset + 6, offset + 8)
        val part3 = toHexaString(pop, littleEndian = true) + "-"
        pop = bytes.copyOfRange(offset + 8, offset + 10)
        val part4 = toHexaString(pop, littleEndian = false) + "-"
        pop = bytes.copyOfRange(offset + 10, offset + 16)
        val part5 = toHexaString(pop, littleEndian = false)
        return StringValue(part1 + part2 + part3 + part4 + part5)
    }

    fun extract(size: Int, type: Type): Value {
        val pop = bytes.copyOfRange(offset, offset + size).toByteArray()
        val buffer = ByteBuffer.wrap(pop)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        val value = when (type) {
            Type.Hexa -> StringValue(toHexaString(buffer.array().toUByteArray(), littleEndian = true))
            Type.String -> StringValue(toAsciiString(buffer.array().toUByteArray()))
            Type.UnSigned -> getUnSigned(size, buffer)
            Type.Signed -> getSigned(size, buffer)
            Type.Bit -> getBit(size, buffer)
            else -> throw IllegalArgumentException()
        }
        return value
    }

    fun getBit(size: Int, raw: ByteBuffer): Value =
            when (size) {
                1 -> FlagOfByte(raw.array()[0])
                2 -> FlagOfWord(raw.getShort())
                4 -> FlagOfDword(raw.getInt())
                8 -> FlagOfQword(raw.getLong())
                else -> throw IllegalArgumentException()
            }

    fun getSigned(size: Int, raw: ByteBuffer): Value =
            when (size) {
                1 -> SignedByte(raw.array()[0])
                2 -> SignedWord(raw.getShort())
                4 -> SignedDword(raw.getInt())
                8 -> SignedQword(raw.getLong())
                else -> throw IllegalArgumentException()
            }

    fun getUnSigned(size: Int, raw: ByteBuffer): Value =
            when (size) {
                1 -> UnsignedByte(raw.array()[0].toUByte())
                2 -> UnsignedWord(raw.getShort().toUShort())
                4 -> UnsignedDword(raw.getInt().toUInt())
                8 -> UnsignedQword(raw.getLong().toULong())
                else -> throw IllegalArgumentException()
            }

    companion object {
        @ExperimentalUnsignedTypes
        fun toAsciiString(bytes: UByteArray): String = bytes.toByteArray().toString(Charsets.UTF_8)

        @ExperimentalUnsignedTypes
        fun toHexaString(bytes: UByteArray, littleEndian: Boolean = false): String {
            var str = ""
            val indexRange = if (littleEndian) bytes.indices.reversed() else bytes.indices
            indexRange.forEach {
                str += "%02X".format(bytes[it].toLong())
            }
            return str
        }

        fun toBinaryString(value: Int): String {
            return Integer.toBinaryString(value)
        }
    }
}