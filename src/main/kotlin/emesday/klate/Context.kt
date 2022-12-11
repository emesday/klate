package emesday.klate

import freemarker.template.*

class Context(val value: String) {

    val baseTemplate: String = "baselayout.ftl"

    val urlFor = singleSimpleScalarParamMethod {
        it.asString
    }

    val static = singleSimpleScalarParamMethod {
        "static/appbuilder/${it.asString}"
    }

    val `_` = singleSimpleScalarParamMethod {
        it.asString
    }
}


fun <T> singleSimpleScalarParamMethod(
    block: (SimpleScalar) -> T,
) = TemplateMethodModelEx { args ->
    val arg = args.first() ?: throw TemplateModelException("wrong arguments")
    when (arg) {
        is SimpleScalar -> block(arg)
        else -> throw TemplateModelException("wrong arguments")
    }
}

fun <T> doubleSimpleScalaParamMethod(
    block: (SimpleScalar, SimpleScalar) -> T,
) = TemplateMethodModelEx { args ->
    val firstArg = args[0] ?: throw TemplateModelException("wrong arguments")
    val secondArg = args[1] ?: throw TemplateModelException("wrong arguments")

    when {
        firstArg is SimpleScalar && secondArg is SimpleScalar -> {
            block(firstArg, secondArg)
        }
        else -> SimpleScalar.EMPTY_STRING
    }
}
