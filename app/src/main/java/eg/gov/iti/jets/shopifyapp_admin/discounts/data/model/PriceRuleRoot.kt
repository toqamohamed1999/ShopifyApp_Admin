package eg.gov.iti.jets.shopifyapp_admin.discounts.data.model

import com.google.gson.annotations.SerializedName

data class PriceRuleRoot(

    @SerializedName("price_rules") var priceRules : ArrayList<PriceRuleX>

)
