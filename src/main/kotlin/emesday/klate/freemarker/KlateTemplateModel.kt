package emesday.klate.freemarker

import emesday.klate.*
import emesday.klate.config.*
import io.ktor.server.application.*

class KlateTemplateModel(application: Application) {

    private val config = application.klate.config

    private fun translate(text: String) = text

    private fun urlFor(url: String) = url

    private val statisUrlPath = config.klate.app.staticUrlPath

    // exposed to sharedVariable

    val baseTemplate = config.klate.app.baseTemplate

    val appTheme = config.klate.app.theme

//    val `_` = singleSimpleScalarParamMethod { translate(it) }

//    val urlFor = singleSimpleScalarParamMethod { urlFor(it) }

//    val static = singleSimpleScalarParamMethod { "$statisUrlPath/$it" }

    val securityManager = application.klate.securityManager
}
