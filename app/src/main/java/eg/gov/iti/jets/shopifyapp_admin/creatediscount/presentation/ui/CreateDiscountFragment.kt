package eg.gov.iti.jets.shopifyapp_admin.creatediscount.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.DiscountCodeB
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.DiscountCodeBody
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.PriceRuleB
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.PriceRuleBody
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.remote.CreateDiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.remote.APIState
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.repo.CreateDiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.presentation.viewmodel.CreateDiscountViewModel
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.presentation.viewmodel.CreateDiscountViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateDiscountBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreateDiscountFragment : Fragment() {

    private val TAG = "CreateDiscountFragment"
    private lateinit var binding : FragmentCreateDiscountBinding

    private val viewModel: CreateDiscountViewModel by lazy {

        val factory = CreateDiscountViewModelFactory(
            CreateDiscountRepoImp.getInstance(CreateDiscountRemoteSourceImp())!!
        )

        ViewModelProvider(this, factory)[CreateDiscountViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateDiscountBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  observeCreateRule()
        //observeRulesData()
       // observeCreateDiscount()
       // observeGetDiscounts()
    }


    private fun observeRulesData() {

        lifecycleScope.launch {
            viewModel.ruleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeRulesData: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "getRulesDataFromApi: $it")
                    }
                }
            }
        }
    }

    private fun observeCreateRule() {

        viewModel.createRule(PriceRuleBody
            (PriceRuleB("test title","line_item",
            "all","across","percentage","-24.0",
            "all",  "2021-21-19T17:59:10Z")))

        lifecycleScope.launch {
            viewModel.createRuleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeRulesData: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "getRulesDataFromApi: $it")
                    }
                }
            }
        }
    }

    private fun observeCreateDiscount() {

        viewModel.createDiscountCode(
            1385615130905,
            DiscountCodeBody(DiscountCodeB("SUMMERSALE10OMM"))
        )

        lifecycleScope.launch {
            viewModel.createDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeRulesData: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "getRulesDataFromApi: $it")
                    }
                }
            }
        }
    }

        private fun observeGetDiscounts() {

            viewModel.getDiscounts(1385615130905)

            lifecycleScope.launch {
                viewModel.getDiscountState.collectLatest {
                    when (it) {
                        is APIState.Loading -> {

                        }
                        is APIState.Success -> {

                            Log.i(TAG, "observeRulesData: ${it.data}")

                        }
                        else -> {

                            Log.i(TAG, "getRulesDataFromApi: $it")
                        }
                    }
                }
            }
    }

}