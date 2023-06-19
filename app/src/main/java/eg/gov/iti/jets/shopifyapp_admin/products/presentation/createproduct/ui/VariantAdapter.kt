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
import eg.gov.iti.jets.shopifyapp_admin.databinding.VarientItemBinding
import eg.gov.iti.jets.shopifyapp_admin.products.data.model.Variant


class VariantAdapter(var variantsList: MutableList<Variant>, private val context: Context) :
    RecyclerView.Adapter<VariantAdapter.ViewHolder>() {

    private val TAG = "ProductsAdapter"
    private lateinit var binding: VarientItemBinding
    private lateinit var holder: ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = VarientItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val variant = variantsList[position]
        this.holder = holder

        if (position == 0) holder.binding.deleteImage.visibility = View.GONE

        holder.binding.variantTextview.text = "Variant " + (position + 1).toString()
        holder.binding.colorEditText.setText(variant.sku)
        holder.binding.sizeEditText.setText(variant.option1)
        holder.binding.priceEditText.setText(variant.price)
        holder.binding.quantityEditText.setText((variant.inventory_quantity ?: "").toString())

        holder.binding.deleteImage.setOnClickListener {
            variantsList.removeAt(position)
            notifyDataSetChanged()
        }

        setupMenu()
        handleSelectedColor(position)
        handleVariantData(position)
    }

    override fun getItemCount() = variantsList.size

    fun setVariantList(values: MutableList<Variant>) {
        this.variantsList = values
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: VarientItemBinding) : RecyclerView.ViewHolder(binding.root)

    private fun setupMenu() {
        val colorsList =
            listOf("white", "black", "orange", "blue", "red", "brown", "yellow", "green")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(context, R.layout.select_dialog_item, colorsList)

        holder.binding.colorEditText.threshold = 1
        holder.binding.colorEditText.setAdapter(adapter)
        holder.binding.colorEditText.setTextColor(Color.BLACK)
    }

    private fun handleSelectedColor(position1: Int) {
        holder.binding.colorEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val item = parent.getItemAtPosition(position)
                variantsList[position1].sku = item.toString()
                variantsList[position1].option2 = item.toString()
            }
    }

    private fun handleVariantData(position1: Int) {
        holder.binding.priceEditText.doAfterTextChanged {
            variantsList[position1].price = it.toString()
        }

        holder.binding.sizeEditText.doAfterTextChanged {
            variantsList[position1].option1 = it.toString()
        }

        holder.binding.quantityEditText.doAfterTextChanged {
           if(!it.toString().isNullOrEmpty())
               variantsList[position1].inventory_quantity = (it.toString()).toInt()
        }
    }

    fun checkDataValidation(): Boolean {
        var variant: Variant
        for (i in variantsList.indices) {
            variant = variantsList[i]

            if (variant.sku.isNullOrEmpty()) {
                holder.binding.colorEditText.error = "should have a color"
                return false
            }
            if (variant.price.isNullOrEmpty()) {
                holder.binding.priceEditText.error = "should have a price"
                return false
            }
            if (variant.option1.isNullOrEmpty()) {
                holder.binding.sizeEditText.error = "should have a size"
                return false
            }
            if (variant.inventory_quantity.toString().isNullOrEmpty()) {
                holder.binding.sizeEditText.error = "should have a quantity"
                return false
            }
        }
        return true
    }
}