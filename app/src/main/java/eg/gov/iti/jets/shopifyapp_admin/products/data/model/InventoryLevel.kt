package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import com.google.gson.annotations.SerializedName

data class InventoryLevel(

    @SerializedName("inventory_item_id") var inventoryItemId: Int? = null,
    @SerializedName("location_id") var locationId: Int? = null,
    @SerializedName("available") var available: Int? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("admin_graphql_api_id") var adminGraphqlApiId: String? = null

)

data class  InventoryLevelResponse(

    @SerializedName("inventory_level" ) var inventoryLevel : InventoryLevel? = InventoryLevel()
)