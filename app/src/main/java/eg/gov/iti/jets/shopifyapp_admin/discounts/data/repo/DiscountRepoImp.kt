package eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo

import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.*
import eg.gov.iti.jets.shopifyapp_admin.discounts.domain.remote.DiscountRemoteSource
import eg.gov.iti.jets.shopifyapp_admin.discounts.domain.repo.DiscountRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DiscountRepoImp private constructor(
    private var remoteSource: DiscountRemoteSource
) :  DiscountRepo {

    companion object {
        private var instance: DiscountRepo? = null

        fun getInstance(
            remoteSource: DiscountRemoteSource,
        ): DiscountRepo? {
            return instance ?: synchronized(this) {
                instance = DiscountRepoImp(remoteSource)

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

    override suspend fun getPriceRules(): Flow<List<PriceRule>> {
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

    override suspend fun updatePriceRule(ruleID: Long, body: PriceRuleResponse): Flow<PriceRuleResponse> {
        return flowOf(remoteSource.updatePriceRule(ruleID,body))
    }

    override suspend fun deletePriceRule(ruleID: Long): Flow<String> {
        return flowOf(remoteSource.deletePriceRule(ruleID))
    }

}