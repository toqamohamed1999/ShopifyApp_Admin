package eg.gov.iti.jets.shopifyapp_admin.createproducts.domain.repo

import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductResponse
import kotlinx.coroutines.flow.Flow

interface CreateProductRepo {

    suspend fun createProduct(body : ProductBody): Flow<ProductResponse>
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun updateProduct(productId: Long, body: ProductResponse): Flow<ProductResponse>
    suspend fun deleteProduct(productId: Long) : Flow<String>
}