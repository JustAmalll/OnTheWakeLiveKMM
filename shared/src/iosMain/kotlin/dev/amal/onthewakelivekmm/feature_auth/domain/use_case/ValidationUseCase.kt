package dev.amal.onthewakelivekmm.feature_auth.domain.use_case

import dev.amal.onthewakelivekmm.feature_auth.domain.models.ValidationResult

actual class ValidationUseCase {

    actual fun validateFirstName(firstName: String): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The first name can't be blank"
            )
        }
        return ValidationResult(successful = true)
    }

    actual fun validateLastName(lastName: String): ValidationResult {
        if (lastName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The last name can't be blank"
            )
        }
        return ValidationResult(successful = true)
    }

    actual fun validatePhoneNumber(phoneNumber: String): ValidationResult {
        if (phoneNumber.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The phone number can't be blank"
            )
        }
        return ValidationResult(successful = true)
    }

    actual fun validatePassword(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password can't be blank"
            )
        }
        if (password.length < 6) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 6 characters"
            )
        }
        return ValidationResult(successful = true)
    }
    
    actual fun validateOtp(otp: String): ValidationResult {
        if (otp.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "OTP cannot be empty"
            )
        }
        return ValidationResult(successful = true)
    }
}