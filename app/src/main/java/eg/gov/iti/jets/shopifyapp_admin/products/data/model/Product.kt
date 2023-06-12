package eg.gov.iti.jets.shopifyapp_admin.products.data.model

data class Product(

    val id: Long? = null,
    val title: String? = null,
    val admin_graphql_api_id: String? = null,
    val body_html: String? = null,
    val created_at: String? = null,
    val handle: String? = null,
    val image: Image? = null,
    val images: List<Image>? = null,
    val options: List<Option>? = null,
    val product_type: String? = null,
    val published_at: String? = null,
    val published_scope: String? = null,
    val status: String? = null,
    val tags: String? = null,
    val template_suffix: Any? = null,
    val updated_at: String? = null,
    val variants: List<Variant>? = null,
    val vendor: String? = null
)