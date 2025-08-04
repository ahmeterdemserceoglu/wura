package com.mis.communication.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mis.communication.R
import com.mis.communication.databinding.ActivityMainBinding
import com.mis.communication.ui.auth.AuthActivity
import com.mis.communication.ui.profile.ProfileActivity
import com.mis.communication.ui.settings.SettingsActivity
import com.mis.communication.utils.PreferenceManager
import com.mis.communication.utils.PermissionUtils

/**
 * Ana Activity - MİS uygulamasının merkezi aktivitesi
 * Sohbetler, Durum, Aramalar ve Kişiler sekmelerini içerir
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager
    
    // ViewPager2 için adapter
    private inner class MainPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ChatsFragment()
                1 -> StatusFragment()
                2 -> CallsFragment()
                3 -> ContactsFragment()
                else -> ChatsFragment()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferenceManager = PreferenceManager(this)
        
        // Kullanıcı giriş durumunu kontrol et
        if (!preferenceManager.isLoggedIn()) {
            startAuthActivity()
            return
        }
        
        setupUI()
        setupViewPager()
        requestRequiredPermissions()
    }

    /**
     * UI bileşenlerini ayarlar
     */
    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        
        // FAB click listener
        binding.fabNewChat.setOnClickListener {
            // Yeni sohbet başlatma işlemi
            startNewChat()
        }
    }

    /**
     * ViewPager2 ve TabLayout'u ayarlar
     */
    private fun setupViewPager() {
        val adapter = MainPagerAdapter(this)
        binding.viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.nav_chats)
                1 -> getString(R.string.nav_status)
                2 -> getString(R.string.nav_calls)
                3 -> getString(R.string.nav_contacts)
                else -> ""
            }
        }.attach()
    }

    /**
     * Gerekli izinleri ister
     */
    private fun requestRequiredPermissions() {
        val permissions = mutableListOf<String>().apply {
            add(Manifest.permission.CAMERA)
            add(Manifest.permission.RECORD_AUDIO)
            add(Manifest.permission.READ_CONTACTS)
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_IMAGES)
                add(Manifest.permission.READ_MEDIA_VIDEO)
                add(Manifest.permission.READ_MEDIA_AUDIO)
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }

        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (!report.areAllPermissionsGranted()) {
                        // İzin verilmeyen durumlar için kullanıcıyı bilgilendir
                        showPermissionRationale()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .check()
    }

    /**
     * İzin gerekçelerini gösterir
     */
    private fun showPermissionRationale() {
        // İzin açıklama dialog'u göster
        PermissionUtils.showPermissionRationale(this)
    }

    /**
     * Yeni sohbet başlatır
     */
    private fun startNewChat() {
        // Kişi seçimi için intent
        // ContactPickerActivity'ye yönlendir
    }

    /**
     * Kimlik doğrulama aktivitesini başlatır
     */
    private fun startAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                // Arama işlevi
                true
            }
            R.id.action_new_group -> {
                // Yeni grup oluşturma
                true
            }
            R.id.action_new_broadcast -> {
                // Yeni yayın listesi
                true
            }
            R.id.action_linked_devices -> {
                // Bağlı cihazlar
                true
            }
            R.id.action_starred_messages -> {
                // Yıldızlı mesajlar
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        // Kullanıcı durumunu online yap
        updateUserStatus(true)
    }

    override fun onPause() {
        super.onPause()
        // Kullanıcı durumunu offline yap
        updateUserStatus(false)
    }

    /**
     * Kullanıcı durumunu günceller
     */
    private fun updateUserStatus(isOnline: Boolean) {
        // Firebase'de kullanıcı durumunu güncelle
        // Son görülme zamanını kaydet
    }
}