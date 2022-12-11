package emesday.klate.validators

import emesday.klate.*
import emesday.klate.exceptions.*

val passwordComplexityRegex = Regex(
    """(
    ^(?=.*[A-Z].*[A-Z])                # at least two capital letters
    (?=.*[^0-9a-zA-Z])                 # at least one of these special characters
    (?=.*[0-9].*[0-9])                 # at least two numeric digits
    (?=.*[a-z].*[a-z].*[a-z])          # at least three lower case letters
    .{10,}                             # at least 10 total characters
    $
    )""",
    RegexOption.COMMENTS
)


fun defaultPasswordComplexity(password: String) {
    passwordComplexityRegex.find(password)
        ?: throw PasswordComplexityValidationError(
            gettext(
                "Must have at least two capital letters," +
                        " one special character, two digits, three lower case letters and" +
                        " a minimal length of 10."
            )
        )
}