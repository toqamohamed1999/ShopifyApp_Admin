package eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model

data class Option(
    val id: Long,
    val name: String,
    val position: Int,
    val product_id: Long,
    val values: List<String>
)