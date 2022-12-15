package emesday.klate.freemarker

import emesday.klate.*
import emesday.klate.config.*
import freemarker.template.*
import io.ktor.server.application.*

fun <T> singleSimpleScalarParamMethod(
    block: (String) -> T,
) = TemplateMethodModelEx { args ->
    val arg = args.first() ?: throw TemplateModelException("wrong arguments")
    when (arg) {
        is SimpleScalar -> block(arg.asString)
        else -> throw TemplateModelException("wrong arguments")
    }
}

fun <T> doubleSimpleScalaParamMethod(
    block: (String, String) -> T,
) = TemplateMethodModelEx { args ->
    val firstArg = args[0] ?: throw TemplateModelException("wrong arguments")
    val secondArg = args[1] ?: throw TemplateModelException("wrong arguments")

    when {
        firstArg is SimpleScalar && secondArg is SimpleScalar -> {
            block(firstArg.asString, secondArg.asString)
        }
        else -> throw TemplateModelException("wrong arguments")
    }
}

class KlateTemplateModel(application: Application) {

    private val config = application.klate.config

    private fun translate(text: String) = text

    private fun urlFor(url: String) = url

    private val statisUrlPath = config.klate.app.staticUrlPath

    // exposed to sharedVariable

    val baseTemplate = config.klate.app.baseTemplate

    val appTheme = config.klate.app.theme

    val `_` = singleSimpleScalarParamMethod { translate(it) }

    val urlFor = singleSimpleScalarParamMethod { urlFor(it) }

    val static = singleSimpleScalarParamMethod { "$statisUrlPath/$it" }

    val securityManager = application.klate.securityManager
}
