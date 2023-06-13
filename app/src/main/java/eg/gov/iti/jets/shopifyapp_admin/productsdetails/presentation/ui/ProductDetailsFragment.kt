package eg.gov.iti.jets.shopifyapp_admin.productsdetails.presentation.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentAllDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentProductDetailsBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentUpdateDiscountBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.DiscountCode
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRule
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui.AllDiscountFragmentArgs
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui.DiscountAdapter
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.viewmodel.AllDiscountsViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.viewmodel.AllDiscountsViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog


class ProductDetailsFragment : Fragment() {

    private val TAG = "ProductDetailsFragment"

    private lateinit var binding: FragmentProductDetailsBinding
    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var product : Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product = args.product

        bindProduct()
    }

    private fun bindProduct(){
        if (product != null) {
            product.images?.let { createDots(it.size) }
            updateDots(0)
            binding.viewPagerImages.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateDots(position)
                }
            })
            binding.progressBar.visibility = View.GONE
            binding.txtProductPrice.text = product.variants?.get(0)?.price ?: ""
            binding.txtProductName.text = product.title
            binding.txtViewDescription.text = product.bodyHtml
            binding.viewPagerImages.adapter = ProductImageViewPagerAdapter(product.images!!)
        }
    }

    private fun createDots(numDots: Int) {
        for (i in 0 until numDots) {
            val dot = View(context)
            val dotSize = 16
            val dotMargin = 8
            val dotParams = LinearLayout.LayoutParams(dotSize, dotSize)
            dotParams.setMargins(dotMargin, 0, dotMargin, 0)
            dot.layoutParams = dotParams
            dot.background = ContextCompat.getDrawable(requireContext(), R.drawable.dot_item)
            binding.dotsLayout.addView(dot)

        }
    }

    private fun updateDots(currentPosition: Int) {
        val numDots = binding.dotsLayout.childCount
        for (i in 0 until numDots) {
            val dot = binding.dotsLayout.getChildAt(i)
            val drawable = if (i == currentPosition) {
                ContextCompat.getDrawable(requireContext(), R.drawable.dot_selected)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.dot_unsetected)
            }
            dot.background = drawable
        }
    }






}