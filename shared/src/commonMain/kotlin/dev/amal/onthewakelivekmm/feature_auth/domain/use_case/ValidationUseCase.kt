package dev.amal.onthewakelivekmm.feature_auth.domain.use_case

import dev.amal.onthewakelivekmm.feature_auth.domain.models.ValidationResult

expect class ValidationUseCase {

    fun validateFirstName(firstName: String): ValidationResult

    fun validateLastName(lastName: String): ValidationResult

    fun validatePhoneNumber(phoneNumber: String): ValidationResult

    fun validatePassword(password: String): ValidationResult
    
    fun validateOtp(otp: String): ValidationResult
}