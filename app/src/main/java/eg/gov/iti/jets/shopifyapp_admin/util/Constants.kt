package eg.gov.iti.jets.shopifyapp_admin.util

import android.app.AlertDialog
import android.content.Context
import android.util.Log
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

fun buildDate(strDate : String) : Date?{
    //val dtStart = "05-11-2017 05:42:00"
    Log.i("CreateRuleFragment", "buildDate: $strDate")
    val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
    try {
        return format.parse(strDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

//fun get12HourFormat(hourOfDay: Int, minute: Int): String {
//    val isPM = hourOfDay >= 12
//    return java.lang.String.format(
//        "%02d:%02d %s",
//        if (hourOfDay === 12 || hourOfDay === 0) 12 else hourOfDay % 12,
//        minute,
//        if (isPM) "PM" else "AM"
//    )
//}


