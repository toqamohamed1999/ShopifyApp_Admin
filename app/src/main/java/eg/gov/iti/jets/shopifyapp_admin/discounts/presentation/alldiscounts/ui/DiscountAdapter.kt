package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.alldiscounts.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.jets.shopifyapp_admin.databinding.DiscountItemBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.DiscountCode
import eg.gov.iti.jets.shopifyapp_admin.util.MyDiffUtil


class DiscountAdapter : ListAdapter<DiscountCode, DiscountAdapter.ArticleViewHolder>(MyDiffUtil<DiscountCode>()) {

    private lateinit var binding: DiscountItemBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DiscountItemBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val discount = getItem(position)

        holder.binding.discountTitleTextView.text = discount.code
       // holder.binding.discountValueTextView.text = disc
    }

    inner class ArticleViewHolder(var binding: DiscountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}