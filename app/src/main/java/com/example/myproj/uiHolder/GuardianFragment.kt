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
import com.example.myproj.dataStore.dataStore
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.model.ApiResult
import com.example.myproj.uiHolder.Injection.provideViewModelFactory
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GuardianFragment() : Fragment() {
    companion object {
        fun newInstance(type: String?) = GuardianFragment()
            .apply {
                arguments = Bundle().apply {
                    putString("type", type)
                }
            }
    }
    private val type: String by lazy { arguments?.getString("type") ?: "Home" }
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
                requireContext().dataStore,
                context = requireContext(),
                owner = this
            )
        ).get(GuardianViewModel::class.java)
        var colorTH = "white"
        var textSize = "medium"
        lifecycleScope.launch {
            viewModel.readFromDataStoreColorTheme.map { color ->
                colorTH = color
            }
        }
        lifecycleScope.launch {
            viewModel.readFromDataStoreTextSize.map { size ->
                textSize = size
            }
        }
        rvAdapter = RvPagingAdapter(requireContext(), type, colorTH, textSize)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter.withLoadStateFooter(
                footer = LoadStateAdapter { rvAdapter.retry() }
            )
            visibility = View.VISIBLE
        }
        when (type) {
            "Home" -> lifecycleScope.launch {
                viewModel.guardianDataHome.collect {
                    uiRender(it)
                }
            }

            "World" -> lifecycleScope.launch {
                viewModel.guardianDataBySectionWorld.collect() {
                    uiRender(it)
                }
            }

            "Science" -> lifecycleScope.launch {
                viewModel.guardianDataBySectionScience.collect() {
                    uiRender(it)
                }
            }

            "Sport" -> lifecycleScope.launch {
                viewModel.guardianDataBySectionSport.collect() {
                    uiRender(it)
                }
            }

            "Environment" -> lifecycleScope.launch {
                viewModel.guardianDataBySectionEnvironment.collect() {
                    uiRender(it)
                }
            }
        }
    }

    private fun uiRender(it: PagingData<ApiResult>) {
        binding.progressBar.visibility = View.GONE
        lifecycleScope.launch {
            rvAdapter.submitData(it)
        }
        lifecycleScope.launch {
            rvAdapter.loadStateFlow.collect { loadState ->
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                binding.recyclerView.isVisible = loadState.refresh !is LoadState.Loading
                binding.tvError.isVisible = loadState.refresh is LoadState.Error
                val errorState = loadState.refresh as? LoadState.Error
                    ?: loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}