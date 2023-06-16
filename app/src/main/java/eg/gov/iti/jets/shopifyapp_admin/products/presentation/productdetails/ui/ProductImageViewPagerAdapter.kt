package eg.gov.iti.jets.shopifyapp_admin.products.presentation.productdetails.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.CouponImageBinding
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Image

class ProductImageViewPagerAdapter(private val images: List<Image>) :
    RecyclerView.Adapter<ProductImageViewPagerAdapter.ViewHolder>() {

    lateinit var binding : CouponImageBinding

    inner class ViewHolder(var binding: CouponImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
       binding = CouponImageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
    fun setImage(image: Image) {

        Glide.with(binding.root.context)
            .load(image.src)
            .error(R.drawable.noimage)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.couponImageView)
    }
    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        setImage(image)

    }
}