package com.example.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.models.Track


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var enteredText: String = ""
    private var tracks: MutableList<Track> = mutableListOf()
    private lateinit var interactor: SearchInteractor
    private lateinit var historyList: List<Track>
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val adapter: TrackAdapter by lazy {
        TrackAdapter(tracks) { track ->

            if (clickDebounce()) {
                interactor.addTrackToHistory(
                    track
                )
                val displayIntent = Intent(this, Player::class.java)
                displayIntent.putExtra(TRACK, track)
                startActivity(displayIntent)
            }
        }
    }
    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter(historyList) { track ->
            if (clickDebounce()) {
                val displayIntent = Intent(this, Player::class.java)
                displayIntent.putExtra(TRACK, track)
                startActivity(displayIntent)
            }
        }
    }

    private val searchRunnable = Runnable {
        if (binding.searchArea.text.isNotEmpty()) {

            doSearch(binding.searchArea.text.toString())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)



        interactor = Creator.provideSearchInteractor()
        historyList = interactor.loadHistoryList()

        binding.searchArea.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doSearch(binding.searchArea.text.toString())
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
                searchDebounce()
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
            interactor.clearHistoryList()
            binding.llSearchHistory.isVisible = false
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
            binding.rvTracks.isVisible = false
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
        hideTrackLists()
        hideKeyboard()
        hidePlaceholder()
        tracks.clear()
        binding.progressBar.isVisible = true

        interactor.doSearch(query, object : SearchInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                runOnUiThread {
                    updateTracksList(foundTracks, interactor.getResponseState())
                }
            }
        })

    }


    fun updateTracksList(trackList: List<Track>, responseState: Int) {


        if (trackList.isNotEmpty()) {
            hidePlaceholder()
            binding.progressBar.isVisible = false
            binding.rvTracks.isVisible = true
            tracks.addAll(trackList)
            adapter.notifyDataSetChanged()
        } else if (responseState == 404) {
            showNotFoundPlaceholder()
        } else {
            showNoInternetPlaceholder()
        }
    }

    private fun showNotFoundPlaceholder() {
        binding.progressBar.isVisible = false
        hideTrackLists()
        adapter.notifyDataSetChanged()
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@SearchActivity,
                R.drawable.not_found
            )
        )
        binding.placeholderText.text = getString(R.string.nothing_found)
    }

    private fun hidePlaceholder() {
        binding.placeholderMessage.isVisible = false
        binding.placeholderButtonRenew.isVisible = false
    }

    private fun hideTrackLists() {
        binding.llSearchHistory.isVisible = false
        binding.rvTracks.isVisible = false
    }


    fun showNoInternetPlaceholder() {
        binding.progressBar.isVisible = false
        hideTrackLists()
        binding.placeholderMessage.isVisible = true
        binding.placeholderImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@SearchActivity,
                R.drawable.no_internet
            )
        )
        binding.placeholderText.text = getString(R.string.no_internet)
        binding.placeholderButtonRenew.isVisible = true
    }

    private fun showHistoryList() {
        historyList = interactor.loadHistoryList()
        historyAdapter.updateData(historyList)
        if (historyList.isNotEmpty()) {
            binding.llSearchHistory.isVisible = true
        } else {
            binding.llSearchHistory.isVisible = false

        }


    }

    private fun searchDebounce() {

        handler.removeCallbacks(searchRunnable)

        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    companion object {
        private const val ENTERED_TEXT = "ENTERED_TEXT"
        private const val TRACK = "TRACK"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}






