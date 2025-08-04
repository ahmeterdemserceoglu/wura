package com.mis.communication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mis.communication.databinding.FragmentContactsBinding
import com.mis.communication.ui.main.adapters.ContactsAdapter

/**
 * Kişiler Fragment
 * Ana ekranın dördüncü sekmesi - Tüm kişileri listeler
 */
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var viewModel: ContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        
        setupRecyclerView()
        setupSearchView()
        observeViewModel()
        
        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshContacts()
        }
        
        // Floating Action Button
        binding.fabInvite.setOnClickListener {
            inviteContacts()
        }
    }

    /**
     * RecyclerView'i ayarlar
     */
    private fun setupRecyclerView() {
        contactsAdapter = ContactsAdapter(
            onContactClick = { contact ->
                // Kişi ile sohbet başlat
                startChatWithContact(contact.id)
            },
            onCallClick = { contact ->
                // Kişiyi ara
                makeCall(contact.id)
            },
            onVideoCallClick = { contact ->
                // Görüntülü arama yap
                makeVideoCall(contact.id)
            },
            onInfoClick = { contact ->
                // Kişi detaylarını göster
                showContactInfo(contact.id)
            }
        )
        
        binding.recyclerViewContacts.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    /**
     * Arama görünümünü ayarlar
     */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchContacts(newText ?: "")
                return true
            }
        })
    }

    /**
     * ViewModel'i gözlemler
     */
    private fun observeViewModel() {
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactsAdapter.submitList(contacts)
            binding.swipeRefresh.isRefreshing = false
            
            // Boş liste kontrolü
            if (contacts.isEmpty()) {
                binding.textViewEmptyState.visibility = View.VISIBLE
                binding.imageViewEmptyState.visibility = View.VISIBLE
                binding.recyclerViewContacts.visibility = View.GONE
            } else {
                binding.textViewEmptyState.visibility = View.GONE
                binding.imageViewEmptyState.visibility = View.GONE
                binding.recyclerViewContacts.visibility = View.VISIBLE
            }
        }
        
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                showError(errorMessage)
            }
        }
        
        // Alfabetik indeks gözlemle
        viewModel.alphabeticIndex.observe(viewLifecycleOwner) { indexMap ->
            // Alfabetik indeks gösterimi için FastScroller kullanılabilir
        }
    }

    /**
     * Kişi ile sohbet başlatır
     */
    private fun startChatWithContact(contactId: String) {
        // ChatActivity'ye yönlendir
        viewModel.createChatWithContact(contactId)
    }

    /**
     * Kişiyi arar
     */
    private fun makeCall(contactId: String) {
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
     * Kişi bilgilerini gösterir
     */
    private fun showContactInfo(contactId: String) {
        // ContactInfoActivity'ye yönlendir
    }

    /**
     * Kişi davet eder
     */
    private fun inviteContacts() {
        // Davet gönderme işlemi
        // Share intent ile MİS uygulaması davetiyesi gönder
    }

    /**
     * Hata mesajını gösterir
     */
    private fun showError(message: String) {
        // Snackbar veya Toast ile hata göster
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadContacts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}