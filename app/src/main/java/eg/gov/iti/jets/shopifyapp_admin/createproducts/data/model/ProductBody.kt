package eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model

import com.google.gson.annotations.SerializedName

data class ProductB(

    @SerializedName("title") var title: String? = null,
    @SerializedName("body_html") var bodyHtml: String? = null,
    @SerializedName("vendor") var vendor: String? = null,
    @SerializedName("product_type") var productType: String? = null,
    @SerializedName("published") var published: Boolean? = null
)


data class ProductBody(

    @SerializedName("product") var product: ProductB? = ProductB()
)