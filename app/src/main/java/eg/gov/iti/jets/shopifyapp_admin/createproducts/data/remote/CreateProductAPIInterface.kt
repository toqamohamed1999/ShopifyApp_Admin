package eg.gov.iti.jets.shopifyapp_admin.createproducts.data.remote

import androidx.room.Delete
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductResponse
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductsRoot
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CreateProductAPIInterface {

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token: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")
    @POST("products.json")
    suspend fun createProduct(
        @Body body: ProductBody
    ): ProductResponse

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token: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")
    @GET("products.json?")
    suspend fun getProducts(): ProductsRoot

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token: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")
    @PUT("products/{product_id}.json")
    suspend fun updateProduct(
        @Path("product_id") productId : Long,
        @Body body: ProductResponse
    ): ProductResponse

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token: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")
    @DELETE("products/{product_id}.json")
    suspend fun deleteProduct(
        @Path("product_id") productId : Long,
    )
}