package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
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
    private var tracksWithMilles: MutableList<TrackWithMilles> = mutableListOf()
    private var tracks: MutableList<Track> = mutableListOf()
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyList: List<Track>
    private val adapter: TrackAdapter by lazy {
        TrackAdapter(tracks) { track ->
            searchHistory.addTrackToHistory(
                track
            )
        }
    }
    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter(historyList) {
        }
    }

    private val iTunesService = RetrofitClient.retrofit.create(iTunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        historyList = searchHistory.loadHistoryList()


        binding.searchArea.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchArea.text.isNotEmpty()) {
                    doSearch(binding.searchArea.text.toString())
                    binding.rvTracks.show()
                }
            }
            true

        }
        binding.placeholderButtonRenew.setOnClickListener {
            doSearch(binding.searchArea.text.toString())
        }


        if (savedInstanceState != null) {
            enteredText = savedInstanceState.getString(ENTERED_TEXT, "")
            binding.searchArea.setText(enteredText)
        }

        binding.clearButton.setOnClickListener() {
            binding.searchArea.setText("")
            tracksWithMilles.clear()
            tracks.clear()
            adapter.notifyDataSetChanged()
            hideKeyboard()
            showHistoryList()
        }

        binding.backButton.setOnClickListener() {
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
                placeholdersAndTracksListVisibility(s)
                enteredText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }


        }
        binding.searchArea.addTextChangedListener(simpleTextWatcher)

        binding.searchArea.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showHistoryList()
        }

        binding.rvTracks.adapter = adapter
        binding.rvTracks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvTracksHistory.adapter = historyAdapter
        binding.rvTracksHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.clearHistoryButton.setOnClickListener() {
            searchHistory.clearHistoryList()
            binding.llSearchHistory.gone()
            searchHistory.clearHistoryList()
        }

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

    private fun placeholdersAndTracksListVisibility(s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            hidePlaceholder()
            binding.rvTracks.gone()
            showHistoryList()
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
        hideKeyboard()
        iTunesService.search(query).enqueue(object :
            Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>,
            ) {

                when (response.code()) {
                    200 -> {
                        updateTracksList(response)
                    }

                    else -> showNoInternetPlaceholder()

                }

            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                showNoInternetPlaceholder()
            }

        })
    }

    fun updateTracksList(response: Response<TracksResponse>) {
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

    fun showNotFoundPlaceholder() {
        tracks.clear()
        adapter.notifyDataSetChanged()
        binding.placeholderMessage.show()
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@SearchActivity,
                R.drawable.not_found
            )
        )
        binding.placeholderText.text = getString(R.string.nothing_found)
    }

    fun hidePlaceholder() {
        binding.placeholderMessage.gone()
        binding.placeholderButtonRenew.gone()
    }


    fun showNoInternetPlaceholder() {
        binding.placeholderMessage.show()
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@SearchActivity,
                R.drawable.no_internet
            )
        )
        binding.placeholderText.text = getString(R.string.no_internet)
        binding.placeholderButtonRenew.show()
    }

    private fun showHistoryList() {
        historyList = searchHistory.loadHistoryList()
        historyAdapter.updateData(historyList)
        if (historyList.isNotEmpty()) {
            binding.llSearchHistory.show()
        } else {
            binding.llSearchHistory.gone()

        }
    }


    companion object {
        private const val ENTERED_TEXT = "ENTERED_TEXT"
    }

}






