package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import com.google.gson.annotations.SerializedName

data class UpdateQuantityBody(

    @SerializedName("location_id") var locationId: Long? = null,
    @SerializedName("inventory_item_id") var inventoryItemId: Long? = null,
    @SerializedName("available") var available: Int? = null

)