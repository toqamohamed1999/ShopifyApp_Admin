package eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model

import com.google.gson.annotations.SerializedName

data class DiscountCodeB(

    @SerializedName("code") var code : String? = null

)

data class DiscountCodeBody (

    @SerializedName("discount_code" ) var discountCode : DiscountCodeB? = DiscountCodeB()

)