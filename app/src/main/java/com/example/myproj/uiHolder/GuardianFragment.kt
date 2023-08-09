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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproj.databinding.FragmentGuardianBinding
import com.example.myproj.loadDataFromInternet.GuardianApiService
import com.example.myproj.loadDataFromInternet.RetrofitIns
import com.example.myproj.model.ApiResult
import com.example.myproj.repository.GuardianRepository
import com.example.myproj.roomDataBase.NewsDataBase
import com.example.myproj.uiHolder.Injection.provideViewModelFactory
import com.example.myproj.uiState.GuardianUiState
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
            this , provideViewModelFactory(
                context = requireContext(),
                owner = this,
                section = type
            )
        ).get(GuardianViewModel::class.java)
        lifecycleScope.launch {
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
    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            // for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            // for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    private suspend fun uIProvider(it: GuardianUiState) {
        when (it) {
            is GuardianUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                rvAdapter = RvPagingAdapter(requireContext())
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = rvAdapter.withLoadStateHeaderAndFooter(
                        header = LoadStateAdapter { rvAdapter.retry() },
                        footer = LoadStateAdapter { rvAdapter.retry() }
                    )
                    visibility = View.VISIBLE
                }
                lifecycleScope.launch {
                    it.data.collectLatest(rvAdapter::submitData)
                }
                lifecycleScope.launch {
                    rvAdapter.loadStateFlow.collect { loadState ->
                        val isListEmpty = loadState.refresh is LoadState.NotLoading && rvAdapter.itemCount == 0
                        binding.tvError.isVisible = loadState.source.append is LoadState.Error
                        binding.recyclerView.isVisible = !isListEmpty
                        binding.tvError.setOnClickListener { rvAdapter.retry() }
                        binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                        val errorState = loadState.source.append as? LoadState.Error
                            ?: loadState.source.prepend as? LoadState.Error
                            ?: loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error
                        errorState?.let {
                            Toast.makeText(
                                requireContext(),
                                "\uD83D\uDE28 Wooops ${it.error}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            else -> {}
        }
    }
}