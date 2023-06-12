package eg.gov.iti.jets.shopifyapp_admin.products.domain.remote

import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductResponse

interface ProductRemoteSource {

    suspend fun createProduct(body : ProductBody): ProductResponse
    suspend fun getProducts(): List<Product>
    suspend fun updateProduct(productId : Long,body: ProductResponse): ProductResponse
    suspend fun deleteProduct(productId : Long) : String

}