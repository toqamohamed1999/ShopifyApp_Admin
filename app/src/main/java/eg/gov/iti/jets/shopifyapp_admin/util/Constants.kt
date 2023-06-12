package eg.gov.iti.jets.shopifyapp_admin.util

import eg.gov.iti.jets.shopifyapp_admin.BuildConfig
import java.util.regex.Pattern

const val BASE_URL = "https://${BuildConfig.api_key}:${BuildConfig.api_password}@${BuildConfig.store_name}.myshopify.com/admin/api/${BuildConfig.api_version}/"

fun getTitleOfProduct(title: String): String {
    val parts = title.split("|").map { it.trim() }
    return if (parts.size >= 2) parts[1] else title
}

