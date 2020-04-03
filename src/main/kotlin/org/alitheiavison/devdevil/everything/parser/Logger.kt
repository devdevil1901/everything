package org.alitheiavison.devdevil.everything.parser

private const val LOG_PREFIX_FORMAT = "[%s:%s]%s::%s(%s): "

fun Any?.log(message: String = "") = this?.let {
    logging(getCaller(), "log:[$message] $it")
}

fun Any?.error(message: String = "") = this?.let {
    logging(getCaller(), "error:[$message] $it")
}

fun logging(trace: StackTraceElement, msg: String) {
    println(buildLog(trace) + ":" + msg)
}

fun buildLog(trace: StackTraceElement): String {
    val dotLast = trace.className.lastIndexOf(".")
    val cName = trace.className.substring(dotLast + 1)

    return String.format(
            LOG_PREFIX_FORMAT,
            "" + Thread.currentThread().id,
            Thread.currentThread().name,
            cName,
            trace.methodName,
            trace.lineNumber
    )
}

fun getCaller(): StackTraceElement {
    val trace = Thread.currentThread().stackTrace
    return trace[5]
}
