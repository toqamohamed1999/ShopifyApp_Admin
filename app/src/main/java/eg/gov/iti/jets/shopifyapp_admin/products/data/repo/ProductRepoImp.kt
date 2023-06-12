package eg.gov.iti.jets.shopifyapp_admin.products.data.repo

import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.ProductResponse
import eg.gov.iti.jets.shopifyapp_admin.products.domain.remote.ProductRemoteSource
import eg.gov.iti.jets.shopifyapp_admin.products.domain.repo.ProductRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductRepoImp private constructor(
    private var remoteSource: ProductRemoteSource
) :  ProductRepo {

    companion object {
        private var instance: ProductRepo? = null

        fun getInstance(
            remoteSource: ProductRemoteSource,
        ): ProductRepo? {
            return instance ?: synchronized(this) {
                instance = ProductRepoImp(remoteSource)

                instance
            }
        }
    }


    override suspend fun createProduct(body: ProductBody): Flow<ProductResponse> {
        return flowOf(remoteSource.createProduct(body))
    }

    override suspend fun getProducts(): Flow<List<Product>> {
        return flowOf(remoteSource.getProducts())
    }

    override suspend fun updateProduct(productId: Long, body: ProductResponse): Flow<ProductResponse>{
        return flowOf(remoteSource.updateProduct(productId,body))
    }

    override suspend fun deleteProduct(productId: Long) : Flow<String> {
        return flowOf(remoteSource.deleteProduct(productId))
    }

}