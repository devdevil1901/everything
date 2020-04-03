package org.alitheiavison.devdevil.everything.parser

import kotlin.IllegalArgumentException
import kotlin.experimental.and

/**
 * kotlin provides unsigned value as blow.
 *
 * kotlin.UByte: an unsigned 8-bit integer, ranges from 0 to 255
 * kotlin.UShort: an unsigned 16-bit integer, ranges from 0 to 65535
 * kotlin.UInt: an unsigned 32-bit integer, ranges from 0 to 2^32 - 1
 * kotlin.ULong: an unsigned 64-bit integer, ranges from 0 to 2^64 - 1
 *
 * see more default: https://kotlinlang.org/docs/reference/whatsnew13.html#unsigned-integers
 */
enum class Type {
    UnSigned, Signed, Bit, String, Hexa, UUID
}

interface Value {
    fun getSize(): Int
    fun getType(): Type
    fun toHexaString(): String
}

abstract class Signed() : Value {
    override fun getType(): Type = Type.Signed
}

abstract class UnSigned() : Value {
    override fun getType(): Type = Type.UnSigned
}

abstract class Bitwise() : Value {
    override fun getType(): Type = Type.Bit

    /**
     * flag have to be format like this -> 0b0100
     */
    abstract fun isSetBit(flag: Any): Boolean
    protected fun isSetBit(mayCompare: Any, flag: Any): Boolean =
            when {
                mayCompare is Byte && flag is Byte -> mayCompare and flag != byteClearBit
                mayCompare is Short && flag is Short -> mayCompare and flag != wordClearBit
                mayCompare is Int && flag is Int -> mayCompare and flag != 0
                mayCompare is Long && flag is Long -> mayCompare and flag != qwordClearBit
                else -> throw IllegalArgumentException()
            }

    companion object {
        const val byteClearBit: Byte = 0
        const val wordClearBit: Short = 0
        const val qwordClearBit: Long = 0
    }
}

@ExperimentalUnsignedTypes
class StringValue(val value: String) : Value {
    override fun getType(): Type = Type.String
    override fun getSize(): Int = value.length
    override fun toHexaString(): String = HexaBuffer.toHexaString(value.toByteArray().toUByteArray())
}

class SignedByte(val value: Byte) : Signed() {
    override fun getSize(): Int = 1
    override fun toHexaString(): String = "0x%02x".format(value.toLong())
}

@ExperimentalUnsignedTypes
class UnsignedByte(val value: UByte) : UnSigned() {
    override fun getSize(): Int = 1
    override fun toHexaString(): String = "0x%02x".format(value.toLong())
}

class SignedWord(val value: Short) : Signed() {
    override fun getSize(): Int = 2
    override fun toHexaString(): String = "0x%02x".format(value)
}

@ExperimentalUnsignedTypes
class UnsignedWord(val value: UShort) : UnSigned() {
    override fun getSize(): Int = 2
    override fun toHexaString(): String = "0x%02x".format(value)
}
class SignedDword(val value: Int) : Signed() {
    override fun getSize(): Int = 4
    override fun toHexaString(): String = "0x%02x".format(value)
}

@ExperimentalUnsignedTypes
class UnsignedDword(val value: UInt) : UnSigned() {
    override fun getSize(): Int = 4
    override fun toHexaString(): String = "0x%02x".format(value)
}

class SignedQword(val value: Long) : Signed() {
    override fun getSize(): Int = 8
    override fun toHexaString(): String = "0x%02x".format(value)
}

@ExperimentalUnsignedTypes
class UnsignedQword(val value: ULong) : UnSigned() {
    override fun getSize(): Int = 8
    override fun toHexaString(): String = "0x%02x".format(value)
}

class FlagOfByte(val value: Byte) : Bitwise() {
    override fun getSize(): Int = 1
    override fun isSetBit(flag: Any): Boolean = isSetBit(value, flag)
    override fun toHexaString(): String = "0x%02x".format(value.toLong())
    fun toBinaryString(): String = Integer.toBinaryString(value.toInt())
}

class FlagOfWord(val value: Short) : Bitwise() {
    override fun getSize(): Int = 2
    override fun isSetBit(flag: Any): Boolean = isSetBit(value, flag)
    override fun toHexaString(): String = "0x%02x".format(value)
    fun toBinaryString(): String = Integer.toBinaryString(value.toInt())
}

class FlagOfDword(val value: Int) : Bitwise() {
    override fun getSize(): Int = 4
    override fun isSetBit(flag: Any): Boolean = isSetBit(value, flag)
    override fun toHexaString(): String = "0x%02x".format(value)
    fun toBinaryString(): String = Integer.toBinaryString(value.toInt())
}

class FlagOfQword(val value: Long) : Bitwise() {
    override fun getSize(): Int = 8
    override fun isSetBit(flag: Any): Boolean = isSetBit(value, flag)
    override fun toHexaString(): String = "0x%02x".format(value)
    fun toBinaryString(): String = java.lang.Long.toBinaryString(value.toLong())
}