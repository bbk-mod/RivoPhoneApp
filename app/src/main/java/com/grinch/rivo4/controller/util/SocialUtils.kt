package com.grinch.rivo4.controller.util

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import java.util.Locale

data class SocialAppInfo(
    val id: String,
    val name: String,
    val packageName: String,
    val iconDrawable: Drawable? = null,
    val assetIcon: String? = null,
    val action: (Context, String) -> Unit
)

object SocialUtils {

    /**
     * Formats a raw phone number to international E.164 format (+15551234567).
     * If formatting fails, cleans formatting symbols and preserves leading '+'.
     */
    fun formatInternationalNumber(context: Context, rawNumber: String): String {
        val trimmed = rawNumber.trim()
        if (trimmed.isEmpty()) return ""

        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        val countryIso = tm?.simCountryIso?.uppercase()?.ifBlank { null }
            ?: tm?.networkCountryIso?.uppercase()?.ifBlank { null }
            ?: Locale.getDefault().country.uppercase().ifBlank { "US" }

        val e164 = try {
            PhoneNumberUtils.formatNumberToE164(trimmed, countryIso)
        } catch (_: Exception) {
            null
        }

        if (!e164.isNullOrBlank()) {
            return e164
        }

        // If it starts with '+', keep '+' and strip any spaces/dashes/parentheses
        return if (trimmed.startsWith("+")) {
            "+" + trimmed.drop(1).replace(Regex("[^0-9]"), "")
        } else {
            trimmed.replace(Regex("[^0-9]"), "")
        }
    }

    /**
     * Extracts only digits without '+' or punctuation for social app deep links.
     */
    fun cleanDigitsOnly(context: Context, rawNumber: String): String {
        val intl = formatInternationalNumber(context, rawNumber)
        return intl.replace(Regex("[^0-9]"), "")
    }

    fun openSignal(context: Context, number: String) {
        val digits = cleanDigitsOnly(context, number)
        if (digits.isBlank()) return
        val url = "sgnl://signal.me/#p/+$digits"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (isPackageInstalled(context, "org.thoughtcrime.securesms")) {
                setPackage("org.thoughtcrime.securesms")
            }
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://signal.me/#p/+$digits")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallbackIntent)
            } catch (_: Exception) {}
        }
    }

    fun openSms(context: Context, number: String) {
        val intl = formatInternationalNumber(context, number)
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$intl")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Queries and returns ONLY the supported social/messaging apps that are
     * actually installed on the user's device.
     */
    fun getInstalledSocialApps(context: Context): List<SocialAppInfo> {
        val pm = context.packageManager
        val list = mutableListOf<SocialAppInfo>()

        // Signal
        if (isPackageInstalled(context, "org.thoughtcrime.securesms")) {
            val appInfo = runCatching { pm.getApplicationInfo("org.thoughtcrime.securesms", 0) }.getOrNull()
            val label = appInfo?.let { pm.getApplicationLabel(it).toString() } ?: "Signal"
            val icon = appInfo?.let { pm.getApplicationIcon(it) }
            list.add(
                SocialAppInfo(
                    id = "signal",
                    name = label,
                    packageName = "org.thoughtcrime.securesms",
                    iconDrawable = icon,
                    assetIcon = "file:///android_asset/icons/signal.png",
                    action = { ctx, num -> openSignal(ctx, num) }
                )
            )
        }

        return list
    }
}

