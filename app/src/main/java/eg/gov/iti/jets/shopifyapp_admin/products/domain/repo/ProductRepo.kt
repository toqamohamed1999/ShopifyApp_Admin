package eg.gov.iti.jets.shopifyapp_admin.products.domain.repo

import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepo {

    suspend fun createProduct(body : ProductBody): Flow<ProductResponse>
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun updateProduct(productId: Long, body: ProductResponse): Flow<ProductResponse>
    suspend fun deleteProduct(productId: Long) : Flow<String>
}