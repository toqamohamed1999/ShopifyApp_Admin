package eg.gov.iti.jets.shopifyapp_admin.util

import android.app.AlertDialog
import android.content.Context
import eg.gov.iti.jets.shopifyapp_admin.BuildConfig
import eg.gov.iti.jets.shopifyapp_admin.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


const val BASE_URL =
    "https://${BuildConfig.api_key}:${BuildConfig.api_password}@${BuildConfig.store_name}.myshopify.com/admin/api/${BuildConfig.api_version}/"

fun getTitleOfProduct(title: String): String {
    val parts = title.split("|").map { it.trim() }
    return if (parts.size >= 2) parts[1] else title
}

fun createAlertDialog(context: Context, msg : String): AlertDialog {
    val builder = AlertDialog.Builder(context)
    builder.setCancelable(false)
    builder.setView(R.layout.loading)

    return builder.create()
}

fun formatDate(strDate : String) : String?{
    //val dtStart = "05-11-2017 05:42 PM"
    val format = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    try {
        return format.parse(strDate).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

