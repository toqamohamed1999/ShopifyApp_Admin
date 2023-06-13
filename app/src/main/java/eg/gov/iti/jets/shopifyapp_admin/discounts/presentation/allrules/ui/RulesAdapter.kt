package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.allrules.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.jets.shopifyapp_admin.databinding.RuleItemBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRule
import eg.gov.iti.jets.shopifyapp_admin.util.MyDiffUtil


class RulesAdapter : ListAdapter<PriceRule, RulesAdapter.ArticleViewHolder>(MyDiffUtil<PriceRule>()) {

    private lateinit var binding: RuleItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RuleItemBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val rule = getItem(position)

        holder.binding.titleTextview.text = rule.title
        holder.binding.valueTextview.text = rule.value+"%"
        holder.binding.createdAtTextview.text = rule.created_at
        if(rule.ends_at.isNullOrEmpty()) holder.binding.endsAtTextview.visibility = View.GONE
        else holder.binding.endsAtTextview.text = rule.ends_at

        holder.binding.ruleCardView.setOnClickListener{
            holder.binding.root.findNavController()
                .navigate(AllRulesFragmentDirections.actionAllRulesFragmentToAllDiscountFragment(rule)
            )
        }
    }

    inner class ArticleViewHolder(var binding: RuleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}