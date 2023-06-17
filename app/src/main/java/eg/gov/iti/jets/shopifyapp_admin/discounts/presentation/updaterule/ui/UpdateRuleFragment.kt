package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updaterule.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import eg.gov.iti.jets.shopifyapp_admin.R
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentCreateRuleBinding
import eg.gov.iti.jets.shopifyapp_admin.databinding.FragmentUpdateRuleBinding
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRule
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRuleB
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRuleBody
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.model.PriceRuleResponse
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.remote.DiscountRemoteSourceImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.data.repo.DiscountRepoImp
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.viewmodel.CreateRuleViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.createrule.viewmodel.CreateRuleViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updaterule.viewmodel.UpdateRuleViewModel
import eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updaterule.viewmodel.UpdateRuleViewModelFactory
import eg.gov.iti.jets.shopifyapp_admin.util.APIState
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class UpdateRuleFragment : Fragment() {

    private val TAG = "UpdateRuleFragment"
    private lateinit var binding: FragmentUpdateRuleBinding
    private val args: UpdateRuleFragmentArgs by navArgs()
    private var priceRule: PriceRule? = null

    private val viewModel: UpdateRuleViewModel by lazy {

        val factory = UpdateRuleViewModelFactory(
            DiscountRepoImp.getInstance(
                DiscountRemoteSourceImp()
            )!!
        )

        ViewModelProvider(this, factory)[UpdateRuleViewModel::class.java]
    }

    private val alertDialog: AlertDialog by lazy {
        createAlertDialog(requireContext(), "")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateRuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        priceRule = args.priceRule

        bindRuleData()
        editActionBtn()
        observeUpdateRule()
        addDatePricker()
    }

    private fun editActionBtn() {
        binding.saveEditBtn.setOnClickListener {
            if (isDataValidate()) {
                buildRule()
                viewModel.updatePriceRule(priceRule?.id!!, PriceRuleResponse(priceRule!!))
                alertDialog.show()
            }
        }
    }

    private fun observeUpdateRule() {
        lifecycleScope.launch {
            viewModel.updateRuleState.collectLatest {
                when (it) {
                    is APIState.Loading -> {
                    }
                    is APIState.Success -> {
                        alertDialog.dismiss()
                        Toast.makeText(requireActivity(), "Updated successfully", Toast.LENGTH_LONG)
                            .show()
                        Navigation.findNavController(binding.root).popBackStack()
                        Log.i(TAG, "observeUpdateRule: ${it.data}")
                    }
                    else -> {
                        alertDialog.dismiss()
                        Log.i(TAG, "observeUpdateRule: $it")
                        Toast.makeText(requireActivity(), "Update failed", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun buildRule() {
        val value = (binding.valueEditText.text.toString()).toDouble() * -1.0

        priceRule?.title = binding.titleEditText.text.toString()
        priceRule?.value = value.toString()
        priceRule?.starts_at = binding.startAtEditText.text.toString()
        priceRule?.ends_at = binding.endsAtEditText.text.toString()
    }

    private fun bindRuleData() {
        val value = ((priceRule?.value?.substring(1))?.toDouble() ?: 1.0)

        if (priceRule != null) {
            binding.titleEditText.setText(priceRule?.title)
            binding.valueEditText.setText(value.toString())
            binding.startAtEditText.setText(priceRule?.starts_at?.substring(0,10))
            binding.endsAtEditText.setText(priceRule?.ends_at?.substring(0,10))
        }
    }

    private fun isDataValidate(): Boolean {
        if (binding.titleEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a title"
            return false
        }
        if (binding.valueEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a value"
            return false
        }
        if ((binding.valueEditText.text.toString()).toDouble() > 100
            || (binding.valueEditText.text.toString()).toDouble() < 1
        ) {
            binding.valueEditText.error = "discount value should be between 1 and 100"
            return false
        }
        if (binding.startAtEditText.text.toString().isNullOrEmpty()) {
            binding.titleEditText.error = "should have a start date"
            return false
        }
        return true
    }

    private fun addDatePricker() {
        binding.startAtEditText.setOnClickListener {
            startDatePicker(it)
        }
        binding.endsAtEditText.setOnClickListener {
            startDatePicker(it)
        }
    }

    private fun startDatePicker(view: View) {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->

                val strDate =
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                if (view.id == binding.startAtEditText.id) {
                    binding.startAtEditText.setText(strDate)
                } else {
                    binding.endsAtEditText.setText(strDate)
                }

            },
            mYear!!,
            mMonth!!,
            mDay!!
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000;
        datePickerDialog.show()
    }
}