package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.allrules.ui

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.jets.shopifyapp_admin.databinding.RuleItemBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRule
import eg.gov.iti.jets.shopifyapp_admin.util.MyDiffUtil


class RulesAdapter : ListAdapter<PriceRule, RulesAdapter.ArticleViewHolder>(MyDiffUtil<PriceRule>()) {

    private val TAG = "RulesAdapter"
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
        if (rule.value_type == "fixed_amount") {
            holder.binding.valueTextview.text = rule.value+" EGP"
        }else{
            holder.binding.valueTextview.text = rule.value+"%"
        }
        holder.binding.createdAtTextview.text = rule.created_at
        holder.binding.startAtTextview.text = Html.fromHtml("<b>Start:</b> ${rule.starts_at}")

        if(rule.ends_at.isNullOrEmpty()) holder.binding.endsAtTextview.text = Html.fromHtml("<b>End:</b> Not determined yet")
        else holder.binding.endsAtTextview.text = Html.fromHtml("<b>End:</b> ${rule.ends_at}")

        holder.binding.ruleCardView.setOnClickListener{
            holder.binding.root.findNavController()
                .navigate(AllRulesFragmentDirections.actionAllRulesFragmentToAllDiscountFragment(rule))
        }
        holder.binding.editImage.setOnClickListener {
            holder.binding.root.findNavController()
                .navigate(AllRulesFragmentDirections.actionAllRulesFragmentToUpdateRuleFragment(rule))
        }
    }

    inner class ArticleViewHolder(var binding: RuleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}