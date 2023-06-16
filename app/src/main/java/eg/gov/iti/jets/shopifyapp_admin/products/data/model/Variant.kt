package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import android.os.Parcelable
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

    ) : Parcelable

@Parcelize
data class VariantRoot(
    var variant: Variant? = null,

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