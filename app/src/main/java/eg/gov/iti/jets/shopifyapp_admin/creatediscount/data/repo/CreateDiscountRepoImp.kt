package eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.repo

import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.domain.remote.CreateDiscountRemoteSource
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.domain.repo.CreateDiscountRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CreateDiscountRepoImp private constructor(
    private var remoteSource: CreateDiscountRemoteSource
) :  CreateDiscountRepo {

    companion object {
        private var instance: CreateDiscountRepo? = null

        fun getInstance(
            remoteSource: CreateDiscountRemoteSource,
        ): CreateDiscountRepo? {
            return instance ?: synchronized(this) {
                instance = CreateDiscountRepoImp(remoteSource)

                instance
            }
        }
    }


    override suspend fun createPriceRule(body: PriceRuleBody): Flow<PriceRuleResponse?> {
        return flowOf(remoteSource.createPriceRule(body))
    }

    override suspend fun createDiscountCode(ruleID : Long, body: DiscountCodeBody): Flow<DiscountCode> {
        return flowOf(remoteSource.createDiscountCode(ruleID, body))
    }

    override suspend fun getPriceRules(): Flow<List<PriceRuleX>> {
        return flowOf(remoteSource.getPriceRules())
    }

    override suspend fun getDiscounts(ruleID: Long): Flow<List<DiscountCode>> {
        return flowOf(remoteSource.getDiscounts(ruleID))
    }

    override suspend fun updateDiscount(
        ruleID: Long,
        discountId: Long,
        body: DiscountCodeResponse
    ): Flow<DiscountCodeResponse> {
        return flowOf(remoteSource.updateDiscount(ruleID,discountId,body))
    }

    override suspend fun deleteDiscount(ruleID: Long, discountId: Long): Flow<String> {
        return flowOf(remoteSource.deleteDiscount(ruleID,discountId))
    }

}