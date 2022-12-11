package emesday.klate.security

import emesday.klate.exceptions.*
import emesday.klate.validators.*
import kotlin.test.*

class PasswordComplexityTest {

    @Test
    fun `defaultComplexityValidator fails`() {
        val passwords = listOf(
            "password",       // No upper, numeric digits or special characters
            "password1234",   // No upper nor special characters
            "password123",    // No upper nor special characters
            "PAssword123",    // Upper, lower, numeric but no special characters
            "PAssw12!",       // Upper, lower, special and numeric but small
            "Password123!",   // Just one Upper
            "PASSWOrd123!",   // Just two lower
            "PAssword3!!",    // Just one numeric digit
            "PAssw3!!",       // length not enough
        )

        for (password in passwords) {
            assertFailsWith(PasswordComplexityValidationError::class) {
                defaultPasswordComplexity(password)
            }
        }
    }

    @Test
    fun `defaultComplexityValidator successes`() {
        val passwords = listOf(
            "PAssword12!",    // Simple valid example
            "PAssword12!//",  // Multiple special characters
            "PAssword12!//>", // More multiple special characters
            "PAssw!ord12",    // Special character in the middle
            "!PAssword12",    // Special characters at the beginning
            "!PAssw>ord12",   // Special characters at the beginning and middle
            "ssw>ord12!PA",   // Upper case in the end
            "ssw>PAord12!",   // Upper case in the end
        )

        for (password in passwords) {
            defaultPasswordComplexity(password)
        }
    }
}
