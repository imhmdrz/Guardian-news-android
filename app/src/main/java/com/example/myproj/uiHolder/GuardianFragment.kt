package com.example.myproj.uiHolder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.loadDataFromInternet.RetrofitIns
import com.example.myproj.model.ApiResult
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.roomDataBase.NewsDataBase
import com.example.myproj.uiState.GuardianUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GuardianFragment(val type: String) : Fragment() {
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
        val factory = GuardianViewModelFactory(GuardianRepository(
            apiService = RetrofitIns.getRetrofitInstance().create(GuardianApiService::class.java),
            db = NewsDataBase.getInstance(requireContext()))
        )
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

    private suspend fun uIProvider(it: GuardianUiState) {
        when (it) {
            is GuardianUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.GONE
                rvAdapter = RvPagingAdapter(requireContext())
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = rvAdapter
                    visibility = View.VISIBLE
                }
                it.data.collect(){
                    rvAdapter.submitData(it)
                }
                rvAdapter.loadStateFlow.collectLatest {
                    if (it.refresh is LoadState.Error) {
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = it.refresh.toString()
                        when(it.refresh){
                            is LoadState.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is LoadState.NotLoading -> {
                                binding.progressBar.visibility = View.GONE
                            }
                            is LoadState.Error -> {
                                binding.progressBar.visibility = View.GONE
                                binding.tvError.visibility = View.VISIBLE
                                binding.tvError.text = it.refresh.toString()
                            }
                        }
                    }
                }
            }

            is GuardianUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
//                binding.tvError.setOnClickListener {
//                    viewModel.refreshData()
//                }
            }

            is GuardianUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.tvError.visibility = View.GONE
            }
        }
    }
}