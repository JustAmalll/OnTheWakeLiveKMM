package dev.amal.onthewakelivekmm.android.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import dev.amal.onthewakelivekmm.android.core.utils.Constants.INSTAGRAM_URL
import dev.amal.onthewakelivekmm.core.util.Constants.ADMIN_IDS

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("No Activity Found")
}

fun Context.openInstagramProfile(instagram: String) = this.startActivity(
    Intent(Intent.ACTION_VIEW, Uri.parse("$INSTAGRAM_URL/$instagram/"))
)

fun String?.isUserAdmin(): Boolean {
    return this in ADMIN_IDS
}