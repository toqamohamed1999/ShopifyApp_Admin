package eg.gov.iti.jets.shopifyapp_admin.createproducts.domain.remote

import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductResponse
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductsRoot
import retrofit2.http.Body
import retrofit2.http.Path

interface CreateProductRemoteSource {

    suspend fun createProduct(body : ProductBody): ProductResponse
    suspend fun getProducts(): List<Product>
    suspend fun updateProduct(productId : Long,body: ProductResponse): ProductResponse
    suspend fun deleteProduct(productId : Long) : String

}