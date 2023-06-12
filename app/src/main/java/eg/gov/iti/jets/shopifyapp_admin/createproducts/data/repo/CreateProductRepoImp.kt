package eg.gov.iti.jets.shopifyapp_admin.createproducts.data.repo

import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.domain.remote.CreateDiscountRemoteSource
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.domain.repo.CreateDiscountRepo
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductBody
import eg.gov.iti.jets.shopifyapp_admin.createproducts.data.model.ProductResponse
import eg.gov.iti.jets.shopifyapp_admin.createproducts.domain.remote.CreateProductRemoteSource
import eg.gov.iti.jets.shopifyapp_admin.createproducts.domain.repo.CreateProductRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class CreateProductRepoImp private constructor(
    private var remoteSource: CreateProductRemoteSource
) :  CreateProductRepo {

    companion object {
        private var instance: CreateProductRepo? = null

        fun getInstance(
            remoteSource: CreateProductRemoteSource,
        ): CreateProductRepo? {
            return instance ?: synchronized(this) {
                instance = CreateProductRepoImp(remoteSource)

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