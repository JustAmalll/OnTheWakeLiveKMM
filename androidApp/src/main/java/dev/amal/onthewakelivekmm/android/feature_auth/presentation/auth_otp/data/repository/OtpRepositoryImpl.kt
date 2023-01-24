package dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.data.repository

import android.content.Context
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dev.amal.onthewakelivekmm.android.core.utils.findActivity
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.model.OtpResult
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.domain.repository.OtpRepository
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OtpRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
): OtpRepository {

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override suspend fun sendOtp(
        phoneNumber: String,
        context: Context,
        isResendAction: Boolean
    ): OtpResult {
        return suspendCoroutine { continuation ->
            try {
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

                    override fun onVerificationFailed(exception: FirebaseException) {
                        if (exception is FirebaseAuthInvalidCredentialsException) {
                            continuation.resume(OtpResult.OtpInvalidCredentials)
                        } else if (exception is FirebaseTooManyRequestsException) {
                            continuation.resume(OtpResult.OtpTooManyRequests)
                        }
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationId, token)

                        storedVerificationId = verificationId
                        resendToken = token

                        continuation.resume(OtpResult.OtpSentSuccess)
                    }
                }

                if (isResendAction) {
                    val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(55L, TimeUnit.SECONDS)
                        .setActivity(context.findActivity())
                        .setCallbacks(callbacks)
                        .setForceResendingToken(resendToken)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(55L, TimeUnit.SECONDS)
                        .setActivity(context.findActivity())
                        .setCallbacks(callbacks)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
            } catch (exception: Exception) {
                continuation.resume(OtpResult.OtpTooManyRequests)
            }
        }
    }

    override suspend fun verifyOtp(
        otp: String
    ): OtpResult = suspendCoroutine { continuation ->
        if (!this::storedVerificationId.isInitialized) {
            continuation.resume(OtpResult.IncorrectOtp)
            return@suspendCoroutine
        }
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            continuation.resume(OtpResult.OtpVerified)
        }.addOnFailureListener {
            continuation.resume(OtpResult.IncorrectOtp)
        }
    }
}