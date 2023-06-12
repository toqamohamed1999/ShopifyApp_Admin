package eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model

import com.google.gson.annotations.SerializedName

data class DiscountCode(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("price_rule_id") var priceRuleId: Long? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("usage_count") var usageCount: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
)

data class DiscountCodeResponse(

    @SerializedName("discount_code") var discountCode: DiscountCode = DiscountCode()
)