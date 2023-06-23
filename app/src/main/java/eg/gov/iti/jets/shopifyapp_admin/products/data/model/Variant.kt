package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Variant(
//inventory quantity/option2(color)/option1(size)/price/title
    var created_at: String? = null,
    var id: Long? = null,
    var image_id: String? = null,
    var inventory_item_id: Long? = null,
    var inventory_management: String? = null,
    var inventory_quantity: Int? = null,
    var old_inventory_quantity: Int? = null,
    var option1: String? = null,
    var option2: String? = null,
    var option3: String? = null,
    var position: Int? = null,
    var price: String? = null,
    var product_id: Long? = null,
    var sku: String? = null,
    var title: String? = null,
    var updated_at: String? = null,
    var tracked: Boolean = true

) : Parcelable

@Parcelize
data class VariantRoot(

    @SerializedName("variant") var variant: Variant? = Variant()

) : Parcelable

/*
@Parcelize
data class Variant1(

    @SerializedName("id") var id: Long? = null,
    @SerializedName("product_id") var productId: Long? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("sku") var sku: String? = null,
    @SerializedName("position") var position: Int? = null,
    @SerializedName("inventory_policy") var inventoryPolicy: String? = null,
    @SerializedName("compare_at_price") var compareAtPrice: String? = null,
    @SerializedName("fulfillment_service") var fulfillmentService: String? = null,
    @SerializedName("inventory_management") var inventoryManagement: String? = null,
    @SerializedName("option1") var option1: String? = null,
    @SerializedName("option2") var option2: String? = null,
    @SerializedName("option3") var option3: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("taxable") var taxable: Boolean? = null,
    @SerializedName("barcode") var barcode: String? = null,
    @SerializedName("grams") var grams: Int? = null,
    @SerializedName("image_id") var imageId: Int? = null,
    @SerializedName("weight") var weight: Double? = null,
    @SerializedName("weight_unit") var weightUnit: String? = null,
    @SerializedName("inventory_item_id") var inventoryItemId: Long? = null,
    @SerializedName("inventory_quantity") var inventoryQuantity: Int? = null,
    @SerializedName("old_inventory_quantity") var oldInventoryQuantity: Int? = null,
    @SerializedName("requires_shipping") var requiresShipping: Boolean? = null,
    @SerializedName("admin_graphql_api_id") var adminGraphqlApiId: String? = null

) : Parcelable
*/