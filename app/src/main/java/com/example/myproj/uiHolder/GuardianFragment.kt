package com.example.myproj.uiHolder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproj.dataStore.dataStore
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.model.ApiResult
import com.example.myproj.uiHolder.Injection.provideViewModelFactory
import com.example.myproj.uiHolder.setting.SettingViewModel
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var settingViewModel: SettingViewModel
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
            requireActivity(), provideViewModelFactory(
                requireActivity().dataStore,
                context = requireActivity(),
                owner = requireActivity()
            )
        ).get(GuardianViewModel::class.java)
        settingViewModel = ViewModelProvider(
            requireActivity(), Injection.provideSettingViewModelFactory(
                requireActivity().dataStore,
                context = requireActivity()
            )
        ).get(SettingViewModel::class.java)
        rvAdapter = RvPagingAdapter(requireContext(), type)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter.withLoadStateFooter(
                footer = LoadStateAdapter { rvAdapter.retry() }
            )
            visibility = View.VISIBLE
        }
        lifecycleScope.launch {
            settingViewModel.isWantReCreate.collect {
                if (it) {
                    viewModel.reCreateDataStore()
                    rvAdapter.refresh()
                    settingViewModel.isWantReCreate.value = false
                    activity?.recreate()
                }
            }
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
                if(loadState.refresh is LoadState.Error){
                    Snackbar.make(binding.root, "No Internet Connection - offline mode", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}