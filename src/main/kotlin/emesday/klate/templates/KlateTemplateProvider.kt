package emesday.klate.templates

import emesday.klate.*
import io.ktor.server.application.*
import kotlin.reflect.*

class KlateTemplateProvider(clazz: KClass<*>) {
}

inline fun <reified T: KlateModule> wrap(): KlateTemplateProvider {
    val clazz = T::class
    return KlateTemplateProvider(clazz)
}

fun template(block: ApplicationCall.() -> KlateTemplate): ApplicationCall.() -> KlateTemplate = block