package eg.gov.iti.jets.shopifyapp_admin.products.domain.remote

import eg.gov.iti.jets.shopifyapp_admin.products.data.model.*
import retrofit2.http.Body
import retrofit2.http.Path

interface ProductRemoteSource {

    suspend fun createProduct(body : ProductBody): ProductResponse
    suspend fun getProducts(): List<Product>
    suspend fun updateProduct(productId : Long,body: ProductResponse): ProductResponse
    suspend fun deleteProduct(productId : Long) : String
    suspend fun updateProductQuantity(body: UpdateQuantityBody) : InventoryLevelResponse
    suspend fun getVariantBYId(variantId : Long) : VariantRoot
}