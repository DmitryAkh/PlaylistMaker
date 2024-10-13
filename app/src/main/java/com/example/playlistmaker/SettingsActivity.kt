package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val isNightMode = sharedPrefs.getBoolean(IS_NIGHT_MODE_KEY, false)
        binding.themeSwitcher.isChecked = isNightMode

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)

        }

        binding.share.setOnClickListener {
            shareApp()
        }

        binding.support.setOnClickListener {
            sendToSupport()
        }

        binding.userAgreement.setOnClickListener {
            openUserAgreement()
        }



        binding.backButton.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun shareApp() {
        val shareText = getString(R.string.share_message)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val intentMessage = getString(R.string.share_intent_message)

        val shareIntent = Intent.createChooser(sendIntent, intentMessage)

        startActivity(shareIntent)
    }

    private fun sendToSupport() {
        val message = getString(R.string.message_to_support)
        val subject = getString(R.string.subject_message_to_support)
        val email = getString(R.string.email)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        startActivity(sendIntent)
    }

    private fun openUserAgreement() {
        val url = getString(R.string.user_agreement_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}