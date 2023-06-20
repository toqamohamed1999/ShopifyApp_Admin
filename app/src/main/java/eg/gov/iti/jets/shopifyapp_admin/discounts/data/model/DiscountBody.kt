package eg.gov.iti.jets.shopifyapp_admin.discounts.data.model

import com.google.gson.annotations.SerializedName

data class DiscountCodeB(

    @SerializedName("code") var code : String
)

data class DiscountCodeBody (

    @SerializedName("discount_code") var discountCode : DiscountCodeB

)