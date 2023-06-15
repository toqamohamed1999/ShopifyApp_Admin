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


class ProductsAdapter(private var productList: List<Product>, private val context: Context,
                      var listener: ProductListener) :
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
        val product = productList[position]

        Glide.with(context)
            .load(product.image?.src)
            .into(holder.binding.productImageView)

        holder.binding.productTitleTextView.text = getTitleOfProduct(product.title!!)
        holder.binding.productPriceTextView.text = (product.variants?.get(0)?.price + " EGP")

        holder.binding.productCardView.setOnClickListener {
            holder.binding.root.findNavController()
                .navigate(AllProductsFragmentDirections
                    .actionAllProductsFragmentToProductDetailsFragment(product))
        }

        holder.binding.deleteImage.setOnClickListener {
            listener.deleteProduct(product)
        }
    }

    override fun getItemCount() = productList.size
    fun changeData(productList: MutableList<Product>) {
        this.productList = productList
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)
}