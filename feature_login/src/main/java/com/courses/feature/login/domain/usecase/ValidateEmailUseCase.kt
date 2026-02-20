package com.courses.feature.login.domain.usecase

import java.util.regex.Pattern

class ValidateEmailUseCase {
    private val emailPattern = Pattern.compile(
        "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    )

    fun execute(email: String): Boolean {
        if (email.isBlank()) return false
        val hasCyrillic = email.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }
        if (hasCyrillic) return false

        return emailPattern.matcher(email).matches()
    }
}