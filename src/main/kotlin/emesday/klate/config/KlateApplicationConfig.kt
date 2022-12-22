package emesday.klate.config

import io.ktor.server.application.*
import io.ktor.server.config.*

private var klateApplicationConfigInstance: KlateApplicationConfig? = null

internal val cache: MutableMap<String, Any> = mutableMapOf()

@Suppress("UNCHECKED_CAST")
internal fun <T> withCache(path: String, block: (String) -> T): T =
    cache[path] as T? ?: block(path)

internal fun invalidateCache(path: String, block: (String) -> Unit) {
    cache.remove(path)
    block(path)
}

fun clearKlateApplicationConfig() {
    cache.clear()
    klateApplicationConfigInstance = null
}

interface KlateApplicationConfigChild {
    val path: String
    val ac: ApplicationConfig
}

internal fun KlateApplicationConfigChild.combine(subPath: String): String = "$path.$subPath"

internal val KlateApplicationConfigChild.klate: KlateApplicationConfig
    get() = ac.klate

internal fun KlateApplicationConfigChild.mapGetter(subPath: String) = withCache(combine(subPath)) { path ->
    ac.keys().filter { it.startsWith(path) }.mapNotNull { key ->
        val realKey = key.removePrefix("$path.")
        val value: String? = try {
            ac.tryGetString(key)
        } catch (ex: Exception) {
            null
        }

        if (value != null) {
            realKey to listOf(value)
        } else {
            val values = try {
                ac.tryGetStringList(key)
            } catch (ex: Exception) {
                null
            }
            if (values != null) {
                realKey to values
            } else {
                null
            }
        }
    }.toMap()
}

internal fun KlateApplicationConfigChild.mapSetter(subPath: String, value: Map<String, List<String>>) {
    invalidateCache(combine(subPath)) { path ->
        for ((k, v) in value) {
            ac.put("$path.$k", v)
        }
    }
}


internal fun <V> KlateApplicationConfigChild.getAsMap(
    path: String,
    getValue: (ApplicationConfig, String) -> V?,
): Map<String, V> = getAsMap(path, { it }, getValue)

internal fun <K, V> KlateApplicationConfigChild.getAsMap(
    path: String,
    getKey: (String) -> K?,
    getValue: (ApplicationConfig, String) -> V?,
): Map<K, V> {
    return ac.keys()
        .filter { it.startsWith(path) }
        .mapNotNull { fullPath ->
            val key = getKey(fullPath.removePrefix("$path."))
            val value = getValue(ac, fullPath)
            if (key != null && value != null) {
                key to value
            } else {
                null
            }
        }
        .toMap()
}

internal fun <K, V> KlateApplicationConfigChild.setMapStringValues(
    path: String,
    getKey: (K) -> String,
    getValue: (V) -> String,
    value: Map<K, V>,
) {

    for ((k, v) in value) {
        ac.put("$path.${getKey(k)}", getValue(v))
    }
}

internal fun <K, V> KlateApplicationConfigChild.setMapListValues(
    path: String,
    getKey: (K) -> String,
    getValue: (V) -> Iterable<String>,
    value: Map<K, V>,
) {

    for ((k, v) in value) {
        ac.put("$path.${getKey(k)}", getValue(v))
    }
}


class KlateApplicationConfig(
    override val ac: ApplicationConfig,
) : KlateApplicationConfigChild {
    override val path: String = "klate"

    private var authInstance: KlateApplicationConfigAuth? = null

    private var dbInstance: KlateApplicationConfigDB? = null

    private var appInstance: KlateApplicationConfigApp? = null

    val auth: KlateApplicationConfigAuth
        get() = run {
            if (authInstance == null) {
                authInstance = KlateApplicationConfigAuth(ac, combine("auth"))
            }
            authInstance!!
        }

    val db: KlateApplicationConfigDB
        get() = run {
            if (dbInstance == null) {
                dbInstance = KlateApplicationConfigDB(ac, combine("db"))
            }
            dbInstance!!
        }

    val app: KlateApplicationConfigApp
        get() = run {
            if (appInstance == null) {
                appInstance = KlateApplicationConfigApp(ac, "app")
            }
            appInstance!!
        }
}

val AC.klate: KlateApplicationConfig
    get() {
        if (klateApplicationConfigInstance == null) {
            klateApplicationConfigInstance = KlateApplicationConfig(this)
        }
        return klateApplicationConfigInstance!!
    }

val Application.ac: ApplicationConfig
    get() = environment.config
