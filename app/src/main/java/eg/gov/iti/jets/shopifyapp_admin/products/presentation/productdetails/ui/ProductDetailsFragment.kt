package eg.gov.iti.jets.shopifyapp_admin.products.presentation.productdetails.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentProductDetailsBinding
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Variant
import eg.gov.iti.jets.shopifyapp_admin.productsdetails.presentation.ui.ProductDetailsFragmentDirections
import eg.gov.iti.jets.shopifyapp_user.products.presentation.ui.DisplayVariantAdapter


class ProductDetailsFragment : Fragment() {

    private val TAG = "ProductDetailsFragment"

    private lateinit var binding: FragmentProductDetailsBinding
    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var product : Product
    private lateinit var displayVariantAdapter: DisplayVariantAdapter
    private var variantList = listOf<Variant>()

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
        editAction()
    }

    private fun setUpRecyclerView() {
        displayVariantAdapter = DisplayVariantAdapter(variantList)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.variantsRecyclerView.layoutManager = layoutManager
        binding.variantsRecyclerView.adapter = displayVariantAdapter
    }

    private fun editAction(){
        binding.editImage.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(
                    ProductDetailsFragmentDirections.actionProductDetailsFragmentToUpdateProductFragment(
                        product
                    )
                )
        }
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
            binding.txtProductPrice.text = (product.variants?.get(0)?.price + " EGP")
            binding.txtProductName.text = product.title
            binding.txtViewDescription.text = product.bodyHtml
            variantList = product.variants
            binding.viewPagerImages.adapter = ProductImageViewPagerAdapter(product.images!!)

            setUpRecyclerView()
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