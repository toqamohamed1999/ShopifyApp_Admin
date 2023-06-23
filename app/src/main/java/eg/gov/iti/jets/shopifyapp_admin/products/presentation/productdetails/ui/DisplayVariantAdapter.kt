package eg.gov.iti.jets.shopifyapp_admin.products.presentation.productdetails.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.jets.shopifyapp_admin.databinding.DisplayVarientItemBinding
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Variant


class DisplayVariantAdapter(private var variantsList: List<Variant>) :
    RecyclerView.Adapter<DisplayVariantAdapter.ViewHolder>() {

    private val TAG = "ProductsAdapter"
    private lateinit var binding: DisplayVarientItemBinding
    private lateinit var holder : ViewHolder


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DisplayVarientItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val variant = variantsList[position]

        holder.binding.variantTextview.text = "Variant "+(position+1).toString()
        // "AD-01-white-6"
        if(variant.sku?.contains("-") == true){
            val list = variant.sku?.split("-")
            holder.binding.colorTextview.text = list?.get(2) ?: ""
        }else{
            holder.binding.colorTextview.text = variant.sku
        }
        holder.binding.sizeTextview.text = variant.option1
        holder.binding.quantityTextview.text = variant.inventory_quantity.toString()
    }

    override fun getItemCount() = variantsList.size

    fun setVariantList(values: List<Variant>) {
        this.variantsList = values
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: DisplayVarientItemBinding) : RecyclerView.ViewHolder(binding.root)

}