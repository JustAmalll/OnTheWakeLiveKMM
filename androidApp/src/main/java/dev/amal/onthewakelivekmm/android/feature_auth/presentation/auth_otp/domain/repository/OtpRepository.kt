package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.repository

import android.content.Context
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.model.OtpResult

interface OtpRepository {

    suspend fun sendOtp(
        phoneNumber: String,
        context: Context,
        isResendAction: Boolean
    ): OtpResult

    suspend fun verifyOtp(otp: String): OtpResult
}