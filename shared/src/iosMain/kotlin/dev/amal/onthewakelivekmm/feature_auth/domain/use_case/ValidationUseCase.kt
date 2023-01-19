package dev.amal.onthewakelivekmm.feature_auth.domain.use_case

import dev.amal.onthewakelivekmm.feature_auth.domain.models.ValidationResult
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

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

    // TODO replace hardcode value 2023
    actual fun validateDateOfBirth(dateOfBirth: String): ValidationResult {
        if (dateOfBirth.isNotEmpty()) {
            if (dateOfBirth.takeLast(4).toInt() >= 2023) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Date of birth can't be greater than the current year"
                )
            }
            if (dateOfBirth.take(2).toInt() > 31) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "Birthday can't be more than the number of days in a month"
                )
            }
            if (dateOfBirth.drop(2).dropLast(4).toInt() > 12) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "The month of birth can't be greater than the number of months in a year"
                )
            }
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

    actual fun validateAdminAddToQueue(
        firstName: String, queue: List<QueueItem>
    ): ValidationResult {
        if (firstName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The first name can't be blank"
            )
        }
        val formattedFirstName = firstName.trim().lowercase()
        val isUserAlreadyInQueue = queue.none {
            it.firstName.lowercase() == formattedFirstName
        }

        if (!isUserAlreadyInQueue) {
            return ValidationResult(
                successful = false,
                errorMessage = "There is already this user in the queue"
            )
        }
        return ValidationResult(successful = true)
    }
}