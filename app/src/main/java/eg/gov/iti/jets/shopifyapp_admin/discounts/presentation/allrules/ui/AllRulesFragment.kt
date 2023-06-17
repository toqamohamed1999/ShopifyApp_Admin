package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.allrules.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentAllRulesBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRule
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.allrules.viewmodel.AllRulesViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.allrules.viewmodel.AllRulesViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AllRulesFragment : Fragment() {

    private val TAG = "AllRulesFragment"

    private lateinit var binding: FragmentAllRulesBinding
    private lateinit var rulesAdapter: RulesAdapter
    private var rulesList: List<PriceRule> = ArrayList()

    private val viewModel: AllRulesViewModel by lazy {

        val factory = AllRulesViewModelFactory(
            DiscountRepoImp.getInstance(
                DiscountRemoteSourceImp()
            )!!)
        ViewModelProvider(this, factory)[AllRulesViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeGetRules()
        addAction()
    }

    override fun onStart() {
        super.onStart()

        viewModel.getPriceRules()
    }

    private fun setUpRecyclerView() {
        rulesAdapter = RulesAdapter()

        binding.rulesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rulesAdapter
        }
    }


    private fun observeGetRules() {
        lifecycleScope.launch {
            viewModel.ruleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                    }
                    is APIState.Success -> {
                        binding.progressbar.visibility = View.GONE
                        Log.i(TAG, "observeGetRules: ${it.data}")
                        rulesList = it.data
                        rulesAdapter.submitList(rulesList)
                    }
                    else -> {
                        binding.progressbar.visibility = View.GONE
                        Log.i(TAG, "observeGetRules: $it")
                    }
                }
            }
        }
    }

    private fun addAction(){
        binding.addFloatingBtn.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.createRuleFragment)
        }
    }

}