package eg.gov.iti.jets.shopifyapp_user.products.presentation.ui

import android.R
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.jets.shopifyapp_admin.databinding.DisplayVarientItemBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.VarientItemBinding
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
        holder.binding.colorTextview.text = variant.option2
        holder.binding.sizeTextview.text = variant.option1
        holder.binding.priceTextview.text = variant.price+ " EGP"
    }

    override fun getItemCount() = variantsList.size

    fun setVariantList(values: List<Variant>) {
        this.variantsList = values
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: DisplayVarientItemBinding) : RecyclerView.ViewHolder(binding.root)

}