package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitcher.isChecked = viewModel.isNightMode

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)

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

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            type = "text/plain"
        }
        val intentMessage = getString(R.string.share_intent_message)

        val shareIntent = Intent.createChooser(sendIntent, intentMessage)

        startActivity(shareIntent)
    }

    private fun sendToSupport() {

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_message_to_support))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.message_to_support))
        }
        startActivity(sendIntent)
    }

    private fun openUserAgreement() {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_url)))
        startActivity(intent)
    }
}