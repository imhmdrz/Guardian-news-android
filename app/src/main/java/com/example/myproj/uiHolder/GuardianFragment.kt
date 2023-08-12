package com.example.myproj.uiHolder

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.model.ApiResult
import com.example.myproj.uiHolder.Injection.provideViewModelFactory
import com.example.myproj.uiState.GuardianUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GuardianFragment(private val type: String) : Fragment() {
    companion object {
        fun newInstance(type: String) = GuardianFragment(type)
    }

    private lateinit var rvAdapter: RvPagingAdapter
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
        viewModel = ViewModelProvider(
            this, provideViewModelFactory(
                context = requireContext(),
                owner = this
            )
        ).get(GuardianViewModel::class.java)
        when (type) {
            "Home" -> lifecycleScope.launch {
                viewModel.guardianData.collect(){
                    uiRender(it)
                }
            }

            "World" -> lifecycleScope.launch {
                viewModel.guardianDataBySection.collect(){
                    uiRender(it)
                }
            }

            "Science" -> lifecycleScope.launch {
                viewModel.guardianDataBySectionScience.collect(){
                    uiRender(it)
                }
            }

            "Sport" -> lifecycleScope.launch {
                viewModel.guardianDataBySectionSport.collect(){
                    uiRender(it)
                }
            }

            "Environment" -> lifecycleScope.launch {
                viewModel.guardianDataBySectionEnvironment.collect(){
                    uiRender(it)
                }
            }
        }
    }

    private fun uiRender(it: PagingData<ApiResult>) {
        binding.progressBar.visibility = View.GONE
        rvAdapter = RvPagingAdapter(requireContext(), type)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter.withLoadStateFooter(
                footer = LoadStateAdapter { rvAdapter.retry() }
            )
            visibility = View.VISIBLE
        }
        lifecycleScope.launch {
            rvAdapter.submitData(it)
        }
        lifecycleScope.launch {
            rvAdapter.loadStateFlow.collect { loadState ->
                binding.tvError.isVisible = loadState.source.append is LoadState.Error
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}