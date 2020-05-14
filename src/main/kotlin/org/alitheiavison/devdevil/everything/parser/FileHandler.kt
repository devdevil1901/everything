package org.alitheiavison.devdevil.everything.parser

import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object FileHandler {
    //  Calls a given block callback, giving it a sequence of all the lines in a file
    //  Once the processing is complete, the file gets closed.
    fun readLine(path: String, processLine: (line: String) -> Unit, charset: Charset = Charsets.UTF_8) =
            File(path).useLines(charset, { line: Sequence<String> ->
                line.forEach {
                    processLine(it)
                }
            })

    fun available(path: String): Boolean {
        val file = File(path)
        if (file.exists() && file.length() != 0L)
            return true
        return false
    }

    fun getSize(path: String): Long = File(path).length()

    @ExperimentalUnsignedTypes
    fun readAll(path: String): UByteArray = File(path).inputStream().use {
        return@use it.readBytes().toUByteArray()
    }

    fun open(path: String): InputStream = File(path).inputStream()
    fun close(inputStream: InputStream) = try {
        inputStream.close()
    } catch (e: IOException) {
        e.message.error()
    }

    fun read(path: String, buffer: ByteArray, offset: Int, size: Int) {
        File(path).inputStream().read(buffer, offset, size)
    }
}

@ExperimentalUnsignedTypes
fun String.getBytesFromResource(): UByteArray? =
        this::class.java.getResourceAsStream(this).use {
            return@use it?.readBytes()?.toUByteArray()
        }