package eg.gov.iti.jets.shopifyapp_admin.products.domain.repo

import eg.gov.iti.jets.shopifyapp_admin.products.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.products.data.remote.ProductRemoteSourceImp
import kotlinx.coroutines.flow.Flow

interface ProductRepo {

    suspend fun createProduct(body : ProductBody): Flow<ProductResponse>
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun updateProduct(productId: Long, body: ProductResponse): Flow<ProductResponse>
    suspend fun deleteProduct(productId: Long) : Flow<String>
    suspend fun updateProductQuantity(body: UpdateQuantityBody) : Flow<InventoryLevelResponse>
    suspend fun getVariantBYId(variantId : Long) : Flow<VariantRoot>


}