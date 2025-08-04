package com.mis.communication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mis.communication.databinding.FragmentChatsBinding
import com.mis.communication.ui.main.adapters.ChatsAdapter

/**
 * Sohbetler Fragment
 * Ana ekranın ilk sekmesi - Tüm sohbetleri listeler
 */
class ChatsFragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var chatsAdapter: ChatsAdapter
    private lateinit var viewModel: ChatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[ChatsViewModel::class.java]
        
        setupRecyclerView()
        observeViewModel()
        
        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshChats()
        }
    }

    /**
     * RecyclerView'i ayarlar
     */
    private fun setupRecyclerView() {
        chatsAdapter = ChatsAdapter { chat ->
            // Sohbete tıklama işlemi
            openChat(chat.id)
        }
        
        binding.recyclerViewChats.apply {
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    /**
     * ViewModel'i gözlemler
     */
    private fun observeViewModel() {
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            chatsAdapter.submitList(chats)
            binding.swipeRefresh.isRefreshing = false
            
            // Boş liste kontrolü
            if (chats.isEmpty()) {
                binding.textViewEmptyState.visibility = View.VISIBLE
                binding.recyclerViewChats.visibility = View.GONE
            } else {
                binding.textViewEmptyState.visibility = View.GONE
                binding.recyclerViewChats.visibility = View.VISIBLE
            }
        }
        
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    /**
     * Sohbeti açar
     */
    private fun openChat(chatId: String) {
        // ChatActivity'ye yönlendir
        // val intent = Intent(requireContext(), ChatActivity::class.java)
        // intent.putExtra("chat_id", chatId)
        // startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadChats()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}