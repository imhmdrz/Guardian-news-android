package com.example.myproj.uiHolder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.model.ApiResult
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.uiState.GuardianUiState
import kotlinx.coroutines.launch

class GuardianFragment(val type: String) : Fragment() {
    companion object {
        fun newInstance(type: String) = GuardianFragment(type)
    }

    private lateinit var viewModel: GuardianViewModel
    private var _binding: FragmentGuardianBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuardianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = GuardianViewModelFactory(GuardianRepository)
        viewModel = ViewModelProvider(
            this,
            factory
        ).get(GuardianViewModel::class.java)
        viewLifecycleOwner.lifecycleScope.launch {
            when (type) {
                "Home" -> viewModel.uiState[0].collect() {
                    uIProvider(it)
                }

                "World" -> viewModel.uiState[1].collect() {
                    uIProvider(it)
                }

                "Science" -> viewModel.uiState[2].collect() {
                    uIProvider(it)
                }

                "Sport" -> viewModel.uiState[3].collect() {
                    uIProvider(it)
                }

                "Environment" -> viewModel.uiState[4].collect() {
                    uIProvider(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uIProvider(it: GuardianUiState) {
        when (it) {
            is GuardianUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.GONE
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = RvAdapter(requireContext(), it.data)
                    visibility = View.VISIBLE
                }
            }

            is GuardianUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.setOnClickListener {
                    viewModel.refreshData()
                }
            }

            is GuardianUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.tvError.visibility = View.GONE
            }
        }
    }
}