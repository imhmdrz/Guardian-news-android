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
import com.example.myproj.MainActivity
import com.example.myproj.R
import com.example.myproj.dataStore.dataStore
import com.example.myproj.databinding.FragmentSettingBinding
import com.example.myproj.uiHolder.GuardianViewModel
import com.example.myproj.uiHolder.Injection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this, Injection.provideSettingViewModelFactory(
                requireContext().dataStore,
                context = requireContext(),
                owner = this
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

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val selectTime = simpleDateFormat.parse(selectedDate)
            if (selectedYear > 2022) {
                Toast.makeText(requireContext(), "Invalid Date Selected", Toast.LENGTH_SHORT).show()
            } else {
                lifecycle.coroutineScope.launch {
                    viewModel.saveToDataStore(fromDate = selectedDate)
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
                viewModel.saveToDataStore(textSize = "small")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnMedium).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(textSize = "medium")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnLarge).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(textSize = "large")
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
                delay(2000)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnSkyBlue).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "skyBlue")
                dialog.dismiss()
                binding.setting.visibility = View.GONE
                binding.progressBarColor.visibility = View.VISIBLE
                delay(2000)
                activity?.recreate()
            }
        }
        dialog.findViewById<View>(R.id.btnDarkBlue).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "darkBlue")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnViolet).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "violet")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnLightGreen).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "lightGreen")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnGreen).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(colorTheme = "green")
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
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnO).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(orderBy = "oldest")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnR).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(orderBy = "relevance")
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
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btn10).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(numberOfItem = "10")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btn15).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStore(numberOfItem = "15")
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