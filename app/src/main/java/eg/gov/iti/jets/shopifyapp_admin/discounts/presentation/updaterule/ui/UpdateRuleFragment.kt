package eg.gov.iti.jets.shopifyapp_admin.discounts.presentation.updaterule.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import eg.gov.iti.jets.shopifyapp_admin.util.buildDate
import eg.gov.iti.jets.shopifyapp_admin.util.createAlertDialog
import eg.gov.iti.jets.shopifyapp_admin.util.get12HourFormat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class UpdateRuleFragment : Fragment() {

    private val TAG = "UpdateRuleFragment"
    private lateinit var binding: FragmentUpdateRuleBinding
    private val args: UpdateRuleFragmentArgs by navArgs()
    private var priceRule: PriceRule? = null
    private var startDate : String? = null
    private var endDate : String? = null
    private var startTime : String? = null
    private var endTime : String? = null
    private var valueType = ""

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
        addTimePicker()
        setupMenu()
        handleSelectedTypeValue()
    }

    private fun setupMenu() {
        val valueTypeList =
            listOf("percentage", "fixed_amount")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, valueTypeList)

        binding.valueTypeEditText.threshold = 1
        binding.valueTypeEditText.setAdapter(adapter)
        binding.valueTypeEditText.setTextColor(Color.BLACK)
    }

    private fun handleSelectedTypeValue() {
        binding.valueTypeEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                valueType = parent.getItemAtPosition(position).toString()
            }
    }
    private fun editActionBtn() {
        binding.saveEditBtn.setOnClickListener {
            if (isDataValidate()) {
                handleEditAction()
            }
        }
    }

    private fun handleEditAction() {
        AlertDialog.Builder(context)
            .setTitle("Edit Rule")
            .setMessage("Are you sure you want to save edit?")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                buildRule()
                viewModel.updatePriceRule(priceRule?.id!!, PriceRuleResponse(priceRule!!))
                alertDialog.show()
            }
            .setNegativeButton("Cancel", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
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
        priceRule?.starts_at = startDate
        priceRule?.ends_at = endDate
        priceRule?.value_type = valueType
    }

    private fun bindRuleData() {
        val value = ((priceRule?.value?.substring(1))?.toDouble() ?: 1.0)

        if (priceRule != null) {
            binding.titleEditText.setText(priceRule?.title)
            binding.valueEditText.setText(value.toString())
            binding.startAtEditText.setText(priceRule?.starts_at?.substring(0,10))
            binding.endsAtEditText.setText(priceRule?.ends_at?.substring(0,10))
            binding.startTimeEditText.setText(priceRule?.starts_at?.substring(11,16))
            binding.endTimeEditText.setText(priceRule?.ends_at?.substring(11,16))
            binding.valueTypeEditText.setText(priceRule?.value_type)
            valueType = priceRule?.value_type!!
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
        if (binding.valueTypeEditText.text.toString().isNullOrEmpty()) {
            binding.valueTypeEditText.error = "should have a value type"
            return false
        }
        if(!validateValue()){
            return false
        }
        if (binding.startAtEditText.text.toString().isNullOrEmpty()) {
            binding.startAtEditText.error = "should have a start date"
            return false
        }
        if (binding.startTimeEditText.text.toString().isNullOrEmpty()) {
            binding.startTimeEditText.error = "should have a start time"
            return false
        }
        if (!binding.endsAtEditText.text.toString().isNullOrEmpty()) {
            if (binding.endTimeEditText.text.toString().isNullOrEmpty()) {
                binding.endTimeEditText.error = "should have a end time"
                return false
            }
        }
        if (!binding.endTimeEditText.text.toString().isNullOrEmpty()) {
            if (binding.endsAtEditText.text.toString().isNullOrEmpty()) {
                binding.endsAtEditText.error = "should have a end date"
                return false
            }
        }
        if(!validateDate()){
            Toast.makeText(requireContext(),"End date before start date",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun validateDate() : Boolean{
        startDate = binding.startAtEditText.text.toString() + " " + binding.startTimeEditText.text
        endDate = binding.endsAtEditText.text.toString() + " " + binding.endTimeEditText.text

        if(!binding.endsAtEditText.text.toString().isNullOrEmpty())
            return buildDate(startDate!!)!!.before(buildDate(endDate!!))

        return true
    }

    private fun validateValue() : Boolean{
        if(valueType == "percentage") {
            if ((binding.valueEditText.text.toString()).toDouble() > 100
                || (binding.valueEditText.text.toString()).toDouble() < 1
            ) {
                binding.valueEditText.error = "discount value should be between 1 and 100"
                return false
            }
        }else{
            if ((binding.valueEditText.text.toString()).toDouble() == 0.0){
                binding.valueEditText.error = "discount value should not be  0"
                return false
            }
        }
        return true
    }

    private fun addDatePricker() {
        binding.startAtEditText.setOnClickListener {
            showDatePicker(it)
        }
        binding.endsAtEditText.setOnClickListener {
            showDatePicker(it)
        }
    }

    private fun addTimePicker() {
        binding.startTimeEditText.setOnClickListener {
            showTimeDialog(it)
        }
        binding.endTimeEditText.setOnClickListener {
            showTimeDialog(it)
        }
    }

    private fun showDatePicker(view: View) {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->

                val strDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
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

    private fun showTimeDialog(view: View) {
        val c2: Calendar = Calendar.getInstance()
        val mHour = c2.get(Calendar.HOUR_OF_DAY)
        val mMinute = c2.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            requireActivity(), { _, hourOfDay, minute ->

                val time = get12HourFormat(hourOfDay, minute)
                if (view.id == binding.startTimeEditText.id) {
                    binding.startTimeEditText.setText(time)
                } else {
                    binding.endTimeEditText.setText(time)
                }

            }, mHour!!, mMinute!!, false
        )
        timePickerDialog.show()
    }


}