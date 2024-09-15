package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Track.Companion.convertTracksWithMillesToTracks
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var enteredText: String = ""
    private var tracksWithMilles = ArrayList<TrackWithMilles>()
    private var tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)

    companion object {
        private const val ENTERED_TEXT = "ENTERED_TEXT"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchArea.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchArea.text.isNotEmpty()) {
                    doSearch(binding.searchArea.text.toString())
                }
            }
            false
        }

        binding.placeholderButtonRenew.setOnClickListener {
            doSearch(binding.searchArea.text.toString())
        }

        if (savedInstanceState != null) {
            enteredText = savedInstanceState.getString(ENTERED_TEXT, "")
            binding.searchArea.setText(enteredText)
        }

        binding.clearButton.setOnClickListener {
            binding.searchArea.setText("")
            hideKeyboard()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = clearButtonVisibility(s)
                enteredText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.searchArea.addTextChangedListener(simpleTextWatcher)

        binding.rvTracks.adapter = adapter
        binding.rvTracks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ENTERED_TEXT, enteredText)
    }

    private fun doSearch(query: String) {
        iTunesService.search(query).enqueue(object :
            Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>,
            ) {

                when (response.code()) {
                    200 -> {
                        tracksWithMilles.clear()
                        tracks.clear()
                        hidePlaceholder()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracksWithMilles.addAll(response.body()?.results!!)
                            tracks.addAll(
                                convertTracksWithMillesToTracks(
                                    tracksWithMilles
                                )
                            )
                            adapter.notifyDataSetChanged()
                        } else {
                            showNotFoundPlaceholder()
                        }
                    }

                    else -> showNoInternetPlaceholder()

                }

            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                showNoInternetPlaceholder()
            }

        })
    }

    fun showNotFoundPlaceholder() {
        tracks.clear()
        adapter.notifyDataSetChanged()
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@SearchActivity,
                R.drawable.not_found
            )
        )
        binding.placeholderText.text = getString(R.string.nothing_found)
    }

    fun hidePlaceholder() {
        binding.placeholderMessage.visibility = View.GONE
        binding.placeholderButtonRenew.visibility = View.GONE
    }

    fun showNoInternetPlaceholder() {
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@SearchActivity,
                R.drawable.no_internet
            )
        )
        binding.placeholderText.text = getString(R.string.no_internet)
        binding.placeholderButtonRenew.visibility = View.VISIBLE
    }


}


