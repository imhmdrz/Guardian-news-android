package com.example.myproj.uiHolder.setting

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.myproj.R
import com.example.myproj.dataStore.dataStore
import com.example.myproj.databinding.FragmentSettingBinding
import com.example.myproj.uiHolder.Injection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class SettingFragment : Fragment() {
    private lateinit var viewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(), Injection.provideSettingViewModelFactory(
                requireActivity().dataStore,
                context = requireActivity()
            )
        ).get(SettingViewModel::class.java)
        lifecycle.coroutineScope.launch {
            viewModel.readFromDataStoreNOI.collect() {
                binding.tvNumber.text = it
            }
        }
        lifecycle.coroutineScope.launch {
            viewModel.readFromDataStoreOrderBy.collect() {
                binding.tvOrderBy.text = it
            }
        }
        lifecycle.coroutineScope.launch {
            viewModel.readFromDataStoreFromDate.collect() {
                binding.tvStartAt.text = it
            }
        }
        lifecycle.coroutineScope.launch {
            viewModel.readFromDataStoreColorTheme.collect(){
                binding.tvColor.text=it
            }
        }
        lifecycle.coroutineScope.launch {
            viewModel.readFromDataStoreTextSize.collect() {
                binding.tvTextSize.text = it
            }
        }

        binding.btnGetNumber.setOnClickListener {
            showDialogForNumberOfItem()
        }
        binding.btnOrderBy.setOnClickListener {
            showDialogForOrderBy()
        }
        binding.btnStartAt.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnColor.setOnClickListener {
            showDialogForColorTheme()
        }
        binding.btnTextSize.setOnClickListener {
            showDialogForTextSize()
        }
    }
    private fun showDatePickerDialog() {
        val myCalendar = Calendar.getInstance()
        val currentYear = myCalendar.get(Calendar.YEAR)
        val currentMonth = myCalendar.get(Calendar.MONTH)
        val currentDayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
            if (selectedYear > 2022) {
                Toast.makeText(requireContext(), "Invalid Date Selected", Toast.LENGTH_SHORT).show()
            } else {
                lifecycle.coroutineScope.launch {
                    viewModel.saveToDataStore(fromDate = selectedDate)
                    viewModel.isWantReCreate.value = true
                }
            }
        }, currentYear, currentMonth, currentDayOfMonth).show()
    }
    private fun showDialogForTextSize() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setContentView(R.layout.text_size_d)
            setCancelable(true)
        }
        dialog.findViewById<View>(R.id.btnSmall).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(textSize = "Small")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(500)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnMedium).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(textSize = "Medium")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(500)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnLarge).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(textSize = "Large")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(500)
                activity?.recreate()
            }
        }
        dialog.apply {
            show()
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)
        }
    }


    private fun showDialogForColorTheme() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setContentView(R.layout.color_theme_d)
            setCancelable(true)
        }
        dialog.findViewById<View>(R.id.btnWhite).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "white")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(1000)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnSkyBlue).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "skyBlue")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(1000)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnDarkBlue).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "darkBlue")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(1000)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnViolet).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "violet")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(1000)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnLightGreen).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "lightGreen")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(1000)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnGreen).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "green")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(1000)
                activity?.recreate()
            }
        }
        dialog.apply {
            show()
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)
        }
    }

    private fun showDialogForOrderBy() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setContentView(R.layout.order_by_d)
            setCancelable(true)
        }
        dialog.findViewById<View>(R.id.btnN).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(orderBy = "newest")
                viewModel.isWantReCreate.value = true
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnO).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(orderBy = "oldest")
                viewModel.isWantReCreate.value = true
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnR).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(orderBy = "relevance")
                viewModel.isWantReCreate.value = true
            }
            dialog.dismiss()
        }
        dialog.apply {
            show()
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)
        }
    }

    private fun showDialogForNumberOfItem() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setContentView(R.layout.number_of_item_d)
            setCancelable(true)
        }
        dialog.findViewById<View>(R.id.btn5).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(numberOfItem = "5")
                viewModel.isWantReCreate.value = true
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btn10).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(numberOfItem = "10")
                viewModel.isWantReCreate.value = true
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btn15).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(numberOfItem = "15")
                viewModel.isWantReCreate.value = true
            }
            dialog.dismiss()
        }
        dialog.apply {
            show()
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            window?.setGravity(Gravity.BOTTOM)
        }
    }
}