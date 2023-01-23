package dev.amal.onthewakelivekmm.android.core.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("No Activity Found")
}

fun Context.openInstagramProfile(instagram: String) = this.startActivity(
    Intent(Intent.ACTION_VIEW, Uri.parse("${Constants.INSTAGRAM_URL}/$instagram/"))
)