package eg.gov.iti.jets.shopifyapp_admin.discounts.data.model

import com.google.gson.annotations.SerializedName

data class PriceRuleBody(

    @SerializedName("price_rule") var priceRule : PriceRuleB
)