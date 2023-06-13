package eg.gov.iti.jets.shopifyapp_admin.products.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val admin_graphql_api_id: String,
    val alt: String,
    val created_at: String,
    val height: Int,
    val id: Long,
    val position: Int,
    val product_id: Long,
    val src: String,
    val updated_at: String,
    val variant_ids: List<String>,
    val width: Int
): Parcelable