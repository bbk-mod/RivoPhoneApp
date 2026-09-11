package com.grinch.rivo4.controller.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object SocialUtils {
    fun openSignal(context: Context, number: String) {
        val url = "sgnl://signal.me/#p/$number"
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: Exception) {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://signal.me/#p/$number")))
            } catch (_: Exception) {}
        }
    }
}
