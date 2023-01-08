package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.model

enum class OtpResult {
    OtpVerified,
    IncorrectOtp,
    OtpInvalidCredentials,
    OtpTooManyRequests,
    OtpSentSuccess
}