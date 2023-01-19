package dev.amal.onthewakelivekmm.feature_auth.domain.use_case

import dev.amal.onthewakelivekmm.feature_auth.domain.models.ValidationResult
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

expect class ValidationUseCase {

    fun validateFirstName(firstName: String): ValidationResult

    fun validateLastName(lastName: String): ValidationResult

    fun validatePhoneNumber(phoneNumber: String): ValidationResult

    fun validateDateOfBirth(dateOfBirth: String): ValidationResult

    fun validatePassword(password: String): ValidationResult

    fun validateOtp(otp: String): ValidationResult

    fun validateAdminAddToQueue(
        firstName: String, queue: List<QueueItem>
    ): ValidationResult
}