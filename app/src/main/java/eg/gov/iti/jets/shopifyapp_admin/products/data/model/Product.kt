package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
//title-body-vendor-type-variant(size/color/price)-option-images
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
    @SerializedName("variants") var variants : List<Variant> = emptyList(),
    @SerializedName("options") var options : List<Option> = emptyList(),
    @SerializedName("images") var images : List<Image> = emptyList(),
    @SerializedName("image") var image : Image = Image()

):Parcelable{
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(image == null){
            result *= 31
        }
        return result
    }
}


//Brands(vendor):
//-------
//ADIDAS
//ASICS TIGER
//CONVERSE
//DR MARTENS
//FLEX FIT
//HERSCHEL
//NIKE
//PALLADUIM
//PUMA
//SUPRA
//TIMBERLAND
//VANS
//=========================================
//Product_Type:
//-------------
//SHOES
//ACCESSORIES
//T-SHIRTS
//==========================================
//Collections(Category):
//----------------------
//KID
//MEN
//SALE
//WOMEN
