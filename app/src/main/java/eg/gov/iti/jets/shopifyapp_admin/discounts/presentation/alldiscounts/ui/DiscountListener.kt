package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui

import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.DiscountCode

interface DiscountListener {

    fun getDiscounts()

    fun updateDiscount(discountCode: DiscountCode)

    fun deleteDiscount(discountCode: DiscountCode)
}