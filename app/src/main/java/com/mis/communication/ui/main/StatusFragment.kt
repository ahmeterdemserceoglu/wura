package com.mis.communication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mis.communication.databinding.FragmentStatusBinding
import com.mis.communication.ui.main.adapters.StatusAdapter

/**
 * Durum Fragment
 * Ana ekranın ikinci sekmesi - Durum güncellemelerini gösterir
 */
class StatusFragment : Fragment() {

    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var statusAdapter: StatusAdapter
    private lateinit var viewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[StatusViewModel::class.java]
        
        setupRecyclerView()
        setupMyStatus()
        observeViewModel()
    }

    /**
     * RecyclerView'i ayarlar
     */
    private fun setupRecyclerView() {
        statusAdapter = StatusAdapter { status ->
            // Duruma tıklama işlemi
            viewStatus(status.id)
        }
        
        binding.recyclerViewStatus.apply {
            adapter = statusAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    /**
     * Kendi durum bölümünü ayarlar
     */
    private fun setupMyStatus() {
        binding.cardMyStatus.setOnClickListener {
            // Kendi durumu görüntüle
            viewMyStatus()
        }
        
        binding.fabAddStatus.setOnClickListener {
            // Yeni durum ekle
            addNewStatus()
        }
    }

    /**
     * ViewModel'i gözlemler
     */
    private fun observeViewModel() {
        viewModel.statusUpdates.observe(viewLifecycleOwner) { statusList ->
            statusAdapter.submitList(statusList)
            
            // Boş liste kontrolü
            if (statusList.isEmpty()) {
                binding.textViewEmptyState.visibility = View.VISIBLE
                binding.recyclerViewStatus.visibility = View.GONE
            } else {
                binding.textViewEmptyState.visibility = View.GONE
                binding.recyclerViewStatus.visibility = View.VISIBLE
            }
        }
        
        viewModel.myStatus.observe(viewLifecycleOwner) { myStatus ->
            if (myStatus != null) {
                binding.textViewMyStatusTime.text = myStatus.timestamp
                binding.imageViewMyStatus.setImageResource(myStatus.imageRes)
                binding.textViewMyStatusIndicator.visibility = View.VISIBLE
            } else {
                binding.textViewMyStatusTime.text = "Durum eklemek için dokunun"
                binding.textViewMyStatusIndicator.visibility = View.GONE
            }
        }
        
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    /**
     * Durumu görüntüler
     */
    private fun viewStatus(statusId: String) {
        // StatusViewerActivity'ye yönlendir
    }

    /**
     * Kendi durumunu görüntüler
     */
    private fun viewMyStatus() {
        // Kendi durum görüntüleyicisine yönlendir
    }

    /**
     * Yeni durum ekler
     */
    private fun addNewStatus() {
        // Durum ekleme aktivitesine yönlendir
        // StatusAddActivity'ye intent gönder
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadStatusUpdates()
        viewModel.loadMyStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}