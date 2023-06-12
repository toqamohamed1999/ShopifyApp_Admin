package eg.gov.iti.jets.shopifyapp_admin.creatediscount.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import eg.gov.iti.jets.shopifyapp_admin.creatediscount.data.model.*
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
        //observeUpdateDiscount()
      //  observeDeleteDiscount()
        //observeUpdateRule()
        observeDeleteRule()
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

                        Log.i(TAG, "observeCreateRule: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeCreateRule: $it")
                    }
                }
            }
        }
    }

    private fun observeCreateDiscount() {

        viewModel.createDiscount(
            1385615130905,
            DiscountCodeBody(DiscountCodeB("SUMMERSALE10OMM"))
        )

        lifecycleScope.launch {
            viewModel.createDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeCreateDiscount: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeCreateDiscount: $it")
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

                            Log.i(TAG, "observeGetDiscounts: ${it.data}")

                        }
                        else -> {

                            Log.i(TAG, "observeGetDiscounts: $it")
                        }
                    }
                }
            }
    }

    private fun observeUpdateDiscount() {

        viewModel.updateDiscount(1385615130905,19605667971353,
        DiscountCodeResponse(DiscountCode(19605667971353, code = "WINTERSALE20OWW"))
        )

        lifecycleScope.launch {
            viewModel.updateDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeUpdateDiscount: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeUpdateDiscount: $it")
                    }
                }
            }
        }
    }

    private fun observeDeleteDiscount() {

        viewModel.deleteDiscount(1385615130905,19605667971353)

        lifecycleScope.launch {
            viewModel.deleteDiscountState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeDeleteDiscount: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeDeleteDiscount: $it")
                    }
                }
            }
        }
    }

    private fun observeUpdateRule() {

        viewModel.updatePriceRule(1385615130905, PriceRuleResponse(
            PriceRuleX(id  = 1385615130905, title = "test title update", value = "-28.0")))

        lifecycleScope.launch {
            viewModel.updateRuleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeUpdateRule: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeUpdateRule: $it")
                    }
                }
            }
        }
    }

    private fun observeDeleteRule() {

        viewModel.deletePriceRule(1385615130905)

        lifecycleScope.launch {
            viewModel.deleteRuleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {

                    }
                    is APIState.Success -> {

                        Log.i(TAG, "observeDeleteRule: ${it.data}")

                    }
                    else -> {

                        Log.i(TAG, "observeDeleteRule: $it")
                    }
                }
            }
        }
    }


}