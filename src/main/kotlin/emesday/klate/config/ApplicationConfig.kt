package emesday.klate.config

import emesday.klate.exceptions.*
import io.ktor.server.config.*

typealias AC = ApplicationConfig

fun AC.getOrElse(path: String, default: String) =
    propertyOrNull(path)?.getString() ?: default

fun AC.getOrElse(path: String, default: List<String>) =
    propertyOrNull(path)?.getList() ?: default

fun AC.string(path: String): String? =
    propertyOrNull(path)?.getString()

fun AC.stringStrict(path: String): String = string(path) ?: throw KlateException()

fun AC.boolean(path: String): Boolean? =
    propertyOrNull(path)?.getString()?.toBoolean()

fun AC.getList(path: String): List<String>? =
    propertyOrNull(path)?.getList()

fun AC.put(path: String, value: String) {
    if (this is MapApplicationConfig) {
        return put(path, value)
    }
    throw KlateException("$this is immutable")
}

fun AC.put(path: String, values: Iterable<String>) {
    if (this is MapApplicationConfig) {
        return put(path, values)
    }
    throw KlateException("$this is immutable")
}

fun <T> AC.put(path: String, value: T) {
    put(path, value.toString())
}
