package dev.amal.onthewakelivekmm.feature_auth.domain.use_case

import android.content.Context
import dev.amal.onthewakelivekmm.R
import dev.amal.onthewakelivekmm.feature_auth.domain.models.ValidationResult

actual class ValidationUseCase(
    private val context: Context
) {

    actual fun validateFirstName(firstName: String): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_first_name_error)
            )
        }
        return ValidationResult(successful = true)
    }

    actual fun validateLastName(lastName: String): ValidationResult {
        if (lastName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_last_name_error)
            )
        }
        return ValidationResult(successful = true)
    }

    actual fun validatePhoneNumber(phoneNumber: String): ValidationResult {
        if (phoneNumber.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage =  context.getString(R.string.validate_phone_number_error)
            )
        }
        return ValidationResult(successful = true)
    }

    actual fun validatePassword(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_password_error)
            )
        }
        if (password.length < 6) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_password_length_error)
            )
        }
        return ValidationResult(successful = true)
    }
    
    actual fun validateOtp(otp: String): ValidationResult {
        if (otp.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validate_otp_error)
            )
        }
        return ValidationResult(successful = true)
    }
}