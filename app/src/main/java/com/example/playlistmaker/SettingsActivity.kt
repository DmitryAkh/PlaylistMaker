package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val share = findViewById<LinearLayout>(R.id.share)
        val support = findViewById<LinearLayout>(R.id.support)
        val userAgreement = findViewById<LinearLayout>(R.id.user_agreement)

        share.setOnClickListener {
            shareApp()
        }

        support.setOnClickListener {
            sendToSupport()
        }

        userAgreement.setOnClickListener {
            openUserAgreement()
        }



        backButton.setOnClickListener {
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