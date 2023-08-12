package com.example.myproj.uiHolder.setting

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.myproj.R
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.databinding.FragmentSettingBinding
import com.example.myproj.uiHolder.GuardianViewModel
import com.example.myproj.uiHolder.Injection
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

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
                binding.tvNumber.text = it.toString()
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
            val dialog = Dialog(requireContext())
            dialog.apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                setContentView(R.layout.number_of_item_d)
                setCancelable(true)
            }
            val bTN5 = dialog.findViewById<View>(R.id.btn5)
            val bTN10 = dialog.findViewById<View>(R.id.btn10)
            val bTN15 = dialog.findViewById<View>(R.id.btn15)
            bTN5.setOnClickListener {
                lifecycle.coroutineScope.launch {
                    viewModel.saveToDataStoreNOI("5")
                }
                dialog.dismiss()
            }
            bTN10.setOnClickListener {
                lifecycle.coroutineScope.launch {
                    viewModel.saveToDataStoreNOI("10")
                }
                dialog.dismiss()
            }
            bTN15.setOnClickListener {
                lifecycle.coroutineScope.launch {
                    viewModel.saveToDataStoreNOI("15")
                }
                dialog.dismiss()
            }
            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
    }
}