package eg.gov.iti.jets.shopifyapp_admin.products.data.remote

import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductResponse
import eg.gov.iti.jets.shopifyapp_admin.products.domain.remote.ProductRemoteSource
import eg.gov.iti.jets.shopifyapp_user.base.remote.AppRetrofit

class ProductRemoteSourceImp : ProductRemoteSource{
    private val TAG = "CreateDiscountRemoteSou"

    private object APIClient{
        val apiInterface : ProductAPIInterface by lazy {
            AppRetrofit.retrofit.create(ProductAPIInterface::class.java)
        }
    }

    override suspend fun createProduct(body: ProductBody): ProductResponse {
        return APIClient.apiInterface.createProduct(body)
    }

    override suspend fun getProducts(): List<Product> {
        return APIClient.apiInterface.getProducts().products
    }

    override suspend fun updateProduct(productId: Long, body: ProductResponse): ProductResponse {
        return APIClient.apiInterface.updateProduct(productId,body)
    }

    override suspend fun deleteProduct(productId: Long) : String {
        return try {
            APIClient.apiInterface.deleteProduct(productId)
            "success"
        }catch (ex : java.lang.Exception){
            "error"
        }
    }

}