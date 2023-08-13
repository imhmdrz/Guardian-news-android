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
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.databinding.FragmentSettingBinding
import com.example.myproj.uiHolder.GuardianViewModel
import com.example.myproj.uiHolder.Injection
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SettingFragment : Fragment() {
    private lateinit var viewModel: GuardianViewModel
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
            this, Injection.provideViewModelFactory(
                context = requireContext(),
                owner = this
            )
        ).get(GuardianViewModel::class.java)
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
            viewModel.readFromDataStoreColorTheme.collect() {
                binding.tvColor.text = it
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
            val currentDate =
                simpleDateFormat.parse("$currentYear-${currentMonth + 1}-$currentDayOfMonth")
            val selectTime = simpleDateFormat.parse(selectedDate)
            val checker = ((currentDate?.time ?: 0) - (selectTime?.time ?: 0)) / (60000)
            if (checker <= 0) {
                Toast.makeText(requireContext(), "Invalid Date Selected", Toast.LENGTH_SHORT).show()
            } else {
                lifecycle.coroutineScope.launch {
                    viewModel.saveToDataStoreFromDate(selectedDate)
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
                viewModel.saveToDataStoreTextSize("small")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnO).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreTextSize("medium")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnR).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreTextSize("large")
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
                viewModel.saveToDataStoreColorTheme("white")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnSkyBlue).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreColorTheme("skyBlue")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnDarkBlue).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreColorTheme("darkBlue")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnViolet).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreColorTheme("violet")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnLightGreen).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreColorTheme("lightGreen")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnGreen).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreColorTheme("green")
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
                viewModel.saveToDataStoreOrderBy("newest")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnO).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreOrderBy("oldest")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btnR).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreOrderBy("relevance")
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
                viewModel.saveToDataStoreNOI("5")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btn10).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreNOI("10")
            }
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btn15).setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.saveToDataStoreNOI("15")
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