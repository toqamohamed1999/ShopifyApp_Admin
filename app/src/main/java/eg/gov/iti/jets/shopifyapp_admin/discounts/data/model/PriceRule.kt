package eg.gov.iti.jets.shopifyapp_admin.discounts.data.model

import com.google.gson.annotations.SerializedName

data class PriceRuleB(

    @SerializedName("title") var title: String? = null,
    @SerializedName("target_type") var targetType: String? = null,
    @SerializedName("target_selection") var targetSelection: String? = null,
    @SerializedName("allocation_method") var allocationMethod: String? = null,
    @SerializedName("value_type") var valueType: String? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("customer_selection") var customerSelection: String? = null,
    @SerializedName("starts_at") var startsAt: String? = null
)