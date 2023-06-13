package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (

    @SerializedName("id") var id : Long?=0,
    @SerializedName("title") var title : String?="",
    @SerializedName("body_html") var bodyHtml : String?="",
    @SerializedName("vendor") var vendor : String?="",
    @SerializedName("product_type") var productType : String?="",
    @SerializedName("created_at") var createdAt : String?="",
    @SerializedName("handle") var handle : String?="",
    @SerializedName("updated_at") var updatedAt : String?="",
    @SerializedName("published_at") var publishedAt : String?="",
    @SerializedName("template_suffix") var templateSuffix : String?="",
    @SerializedName("status") var status : String?="",
    @SerializedName("published_scope") var publishedScope : String?="",
    @SerializedName("tags") var tags : String?="",
    @SerializedName("admin_graphql_api_id") var adminGraphqlApiId : String?="",
    @SerializedName("variants") var variants : List<Variant>,
    @SerializedName("options") var options : List<Option>,
    @SerializedName("images") var images : List<Image>,
    @SerializedName("image") var image : Image

//    val id: Long? = null,
//    val title: String? = null,
//    val admin_graphql_api_id: String? = null,
//    val body_html: String? = null,
//    val created_at: String? = null,
//    val handle: String? = null,
//    val image: Image? = null,
//    val images: List<Image>? = null,
//    val options: List<Option>? = null,
//    val product_type: String? = null,
//    val published_at: String? = null,
//    val published_scope: String? = null,
//    val status: String? = null,
//    val tags: String? = null,
//    val updated_at: String? = null,
//    val variants: List<Variant>? = null,
//    val vendor: String? = null
): Parcelable