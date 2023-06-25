package eg.gov.iti.jets.shopifyapp_admin.products.data.remote

import eg.gov.iti.jets.shopifyapp_admin.BuildConfig
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductAPIInterface {

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+BuildConfig.admin_passowrd)
    @POST("products.json")
    suspend fun createProduct(
        @Body body: ProductBody
    ): ProductResponse

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+BuildConfig.admin_passowrd)
    @GET("products.json?")
    suspend fun getProducts(): ProductsRoot

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+BuildConfig.admin_passowrd)
    @PUT("products/{product_id}.json")
    suspend fun updateProduct(
        @Path("product_id") productId : Long,
        @Body body: ProductResponse
    ): ProductResponse

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+BuildConfig.admin_passowrd)
    @DELETE("products/{product_id}.json")
    suspend fun deleteProduct(
        @Path("product_id") productId : Long
    )

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+BuildConfig.admin_passowrd)
    @POST("inventory_levels/set.json")
    suspend fun updateProductQuantity(
        @Body body: UpdateQuantityBody
    ) : InventoryLevelResponse

    @Headers("Content-Type:application/json", "X-Shopify-Access-Token:"+BuildConfig.admin_passowrd)
    @GET("variants/{variant_id}.json")
    suspend fun getVariantBYId(
        @Path("variant_id") VariantId : Long
    ) : VariantRoot
}