package eg.gov.iti.jets.shopifyapp_user.products.presentation.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Product
import eg.gov.iti.jets.shopifyapp_admin.databinding.ProductItemBinding
import eg.gov.iti.jets.shopifyapp_admin.products.presentation.allproducts.ui.AllProductsFragmentDirections
import eg.gov.iti.jets.shopifyapp_admin.util.getTitleOfProduct


class ProductsAdapter(private var productList: List<Product>, private val context: Context, var myListener: ProductListener) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private val TAG = "ProductsAdapter"
    private lateinit var binding: ProductItemBinding

    fun setProductList(values: List<Product?>?) {
        this.productList = values as List<Product>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ProductItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = productList[position]

        holder.binding.productTitleTextView.text = getTitleOfProduct(currentProduct.title!!)
        holder.binding.productPriceTextView.text = currentProduct.variants?.get(0)?.price ?: ""
        Glide.with(context)
            .load(currentProduct.image?.src)
            .into(holder.binding.productImageView)

        Log.i(TAG, "onBindViewHolder: $currentProduct")
        holder.binding.productCardView.setOnClickListener {
            holder.binding.root.findNavController()
                .navigate(AllProductsFragmentDirections
                    .actionAllProductsFragmentToProductDetailsFragment(currentProduct))
        }
    }

    override fun getItemCount() = productList.size

    inner class ViewHolder(var binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)
}