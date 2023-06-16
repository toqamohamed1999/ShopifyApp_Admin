package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import com.google.gson.annotations.SerializedName

data class ProductB(

    @SerializedName("title") var title: String? = null,
    @SerializedName("body_html") var bodyHtml: String? = null,
    @SerializedName("vendor") var vendor: String? = null,
    @SerializedName("product_type") var productType: String? = null,
    @SerializedName("published") var published: Boolean? = null,
    @SerializedName("variants") var variants : List<Variant> = emptyList(),
    @SerializedName("images") var images: ArrayList<Image> = arrayListOf()
)


data class ProductBody(

    @SerializedName("product") var product: ProductB
)