package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Variant(

    val created_at: String? = null,
    val id: Long? = null,
    val image_id: String? = null,
    val inventory_item_id: Long? = null,
    val inventory_management: String? = null,
    val inventory_quantity: Int? = null,
    val old_inventory_quantity: Int? = null,
    val option1: String? = null,
    val option2: String? = null,
    val option3: String? = null,
    val position: Int? = null,
    val price: String? = null,
    val product_id: Long? = null,
    val sku: String? = null,
    val title: String? = null,
    val updated_at: String? = null,

) : Parcelable
/*
{
    "inventory_item": {
    "id": 808950810,
    "sku": "IPOD2008PINK",
    "created_at": "2020-01-14T10:41:30-05:00",
    "updated_at": "2020-01-14T10:41:30-05:00",
    "requires_shipping": true,
    "cost": "25.00",
    "country_code_of_origin": null,
    "province_code_of_origin": null,
    "harmonized_system_code": null,
    "tracked": true,
    "country_harmonized_system_codes": [],
    "admin_graphql_api_id": "gid://shopify/InventoryItem/808950810"
    }

}

 */