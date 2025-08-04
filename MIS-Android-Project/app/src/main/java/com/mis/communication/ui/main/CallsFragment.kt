package com.mis.communication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mis.communication.databinding.FragmentCallsBinding
import com.mis.communication.ui.main.adapters.CallsAdapter

/**
 * Aramalar Fragment
 * Ana ekranın üçüncü sekmesi - Arama geçmişini gösterir
 */
class CallsFragment : Fragment() {

    private var _binding: FragmentCallsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var callsAdapter: CallsAdapter
    private lateinit var viewModel: CallsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[CallsViewModel::class.java]
        
        setupRecyclerView()
        observeViewModel()
        
        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshCalls()
        }
    }

    /**
     * RecyclerView'i ayarlar
     */
    private fun setupRecyclerView() {
        callsAdapter = CallsAdapter(
            onCallClick = { call ->
                // Arama detayına git
                viewCallDetails(call.id)
            },
            onCallBack = { call ->
                // Geri arama yap
                makeCall(call.contactId, call.type)
            },
            onVideoCall = { call ->
                // Görüntülü arama yap
                makeVideoCall(call.contactId)
            }
        )
        
        binding.recyclerViewCalls.apply {
            adapter = callsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    /**
     * ViewModel'i gözlemler
     */
    private fun observeViewModel() {
        viewModel.calls.observe(viewLifecycleOwner) { calls ->
            callsAdapter.submitList(calls)
            binding.swipeRefresh.isRefreshing = false
            
            // Boş liste kontrolü
            if (calls.isEmpty()) {
                binding.textViewEmptyState.visibility = View.VISIBLE
                binding.imageViewEmptyState.visibility = View.VISIBLE
                binding.recyclerViewCalls.visibility = View.GONE
            } else {
                binding.textViewEmptyState.visibility = View.GONE
                binding.imageViewEmptyState.visibility = View.GONE
                binding.recyclerViewCalls.visibility = View.VISIBLE
            }
        }
        
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                // Hata mesajını göster
                showError(errorMessage)
            }
        }
    }

    /**
     * Arama detaylarını görüntüler
     */
    private fun viewCallDetails(callId: String) {
        // CallDetailsActivity'ye yönlendir
    }

    /**
     * Sesli arama yapar
     */
    private fun makeCall(contactId: String, callType: String) {
        // VoiceCallActivity'ye yönlendir
        viewModel.initiateVoiceCall(contactId)
    }

    /**
     * Görüntülü arama yapar
     */
    private fun makeVideoCall(contactId: String) {
        // VideoCallActivity'ye yönlendir
        viewModel.initiateVideoCall(contactId)
    }

    /**
     * Hata mesajını gösterir
     */
    private fun showError(message: String) {
        // Snackbar veya Toast ile hata göster
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCalls()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}