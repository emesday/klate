package emesday.klate.exceptions

open class KlateException(message: String? = null) : Exception(message)

class PasswordComplexityValidationError(message: String? = null) : KlateException(message)

