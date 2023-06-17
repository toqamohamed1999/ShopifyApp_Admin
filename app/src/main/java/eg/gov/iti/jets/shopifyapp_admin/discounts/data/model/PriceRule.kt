package eg.gov.iti.jets.shopifyapp_admin.discounts.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceRule(

    val allocation_limit: String? = null,
    val allocation_method: String? = null,
    val created_at: String? = null,
    val customer_selection: String? = null,
    var ends_at: String? = null,
    val id: Long? = null,
    val once_per_customer: Boolean? = null,
    var starts_at: String? = null,
    val target_selection: String? = null,
    val target_type: String? = null,
    var title: String? = null,
    val updated_at: String? = null,
    val usage_limit: String? = null,
    var value: String? = null,
    val value_type: String? = null,
) : Parcelable