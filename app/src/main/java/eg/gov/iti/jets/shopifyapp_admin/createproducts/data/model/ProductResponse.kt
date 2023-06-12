package eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model

import com.google.gson.annotations.SerializedName



data class ProductResponse(

    @SerializedName("product") var product: Product
)