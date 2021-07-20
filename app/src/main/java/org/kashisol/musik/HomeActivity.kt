package org.kashisol.musik

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.kashisol.musik.viewmodel.ITuneResultAdapter
import org.kashisol.musik.model.APIReceiver
import org.kashisol.musik.model.ITuneResults
import org.kashisol.musik.repository.LocalDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeActivity : AppCompatActivity(), LocalDB.OnLocalDBResult {

    private var forceOfflineSearch = true
    private var snackMessage: String = ""

    private lateinit var searchResultsList: ArrayList<ITuneResults>
    private lateinit var iTunesResultAdapter: ITuneResultAdapter
//    private lateinit var db: Database
//    private lateinit var dao: ITuneResultDao

    private lateinit var search_field: EditText
    private lateinit var result_recycler_view: RecyclerView
    private lateinit var searching_progress_field: ProgressBar
    private lateinit var search_button: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // initializing the non-ui elements
        searchResultsList = ArrayList()
        iTunesResultAdapter = ITuneResultAdapter(searchResultsList, applicationContext)
        LocalDB.initializeDB(applicationContext, this)

        // initializing the widgets
        search_field = findViewById(R.id.search_field)
        result_recycler_view = findViewById(R.id.result_recycler_view)
        searching_progress_field = findViewById(R.id.searching_progress_field)
        search_button = findViewById(R.id.search_button)

        // setting up the key listener on search field so that whenever the user performs an action
        // of entering the term/value in the field, the searching process must start all over again.
        search_field.setOnKeyListener { _, i, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                // if the input is null or empty, then show toast with message
                if (search_field.text.toString().isEmpty()) {
                    // hide the keyboard
                    hideKeyboard(this@HomeActivity)
                    Snackbar.make(result_recycler_view, "Please enter value to search", 1000).show()
                    return@setOnKeyListener false
                }
                // whenever a successful search is started, show the progress bar
                searching_progress_field.visibility = View.VISIBLE
                // hide the keyboard
                hideKeyboard(this@HomeActivity)
                searchAndShow(search_field.text.toString().trim())
                true
            } else {
                false
            }
        }

        search_button.setOnClickListener(View.OnClickListener { v ->
            // if the input is null or empty, then show toast with message
            if (search_field.text.toString().isEmpty()) {
                // hide the keyboard
                hideKeyboard(this@HomeActivity)
                Snackbar.make(v, "Please enter value to search", 1000).show()
                return@OnClickListener
            }
            // whenever a successful search is started, show the progress bar
            searching_progress_field.visibility = View.VISIBLE
            // hide the keyboard
            hideKeyboard(this@HomeActivity)
            searchAndShow(search_field.text.toString().trim())
        })

        // Setting up the layout manager and adapter on recycler view
        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        result_recycler_view.layoutManager = gridLayoutManager
        result_recycler_view.adapter = iTunesResultAdapter

    }

    private fun searchAndShow(term: String) {
        val retro: Retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api: APIReceiver = retro.create(APIReceiver::class.java)
        val call: Call<JsonObject> = api.getSearchResponse(term)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                println("()()()()dadasdas : " + response.body().toString())
                // Get the JSON response only of the required thing i.e "result"
                val results : JsonElement? = response.body()?.get("results")
                // get the json element as array
                val requiredLists = results?.asJsonArray

                // code to run before the search list gets updated.
                beforeSearchListUpdated()

                // if the result we get is not null then,
                if (requiredLists != null) {
                    for (elem in requiredLists) {
                        val obj = elem.asJsonObject

                        // object in which all the data will be added
                        var toAdd: ITuneResults = ITuneResults()
                        if (obj["wrapperType"] != null)
                            toAdd.wrapperType = obj["wrapperType"].asString
                        if (obj["kind"] != null)
                            toAdd.kind = obj["kind"].asString
                        if (obj["artistId"] != null)
                            toAdd.artistId = obj["artistId"].asInt
                        if (obj["collectionId"] != null)
                            toAdd.collectionId = obj["collectionId"].asInt
                        if (obj["trackId"] != null)
                            toAdd.trackId = obj["trackId"].asInt
                        if (obj["artistName"] != null)
                            toAdd.artistName = obj["artistName"].asString
                        if (obj["collectionName"] != null)
                            toAdd.collectionName = obj["collectionName"].asString
                        if (obj["trackName"] != null)
                            toAdd.trackName = obj["trackName"].asString
                        if (obj["collectionCensoredName"] != null)
                            toAdd.collectionCensoredName = obj["collectionCensoredName"].asString
                        if (obj["trackCensoredName"] != null)
                            toAdd.trackCensoredName = obj["trackCensoredName"].asString
                        if (obj["artistViewUrl"] != null)
                            toAdd.artistViewUrl = obj["artistViewUrl"].asString
                        if (obj["collectionViewUrl"] != null)
                            toAdd.collectionViewUrl = obj["collectionViewUrl"].asString
                        if (obj["trackViewUrl"] != null)
                            toAdd.trackViewUrl = obj["trackViewUrl"].asString
                        if (obj["previewUrl"] != null)
                            toAdd.previewUrl = obj["previewUrl"].asString
                        if (obj["artworkUrl30"] != null)
                            toAdd.artworkUrl30 = obj["artworkUrl30"].asString
                        if (obj["artworkUrl60"] != null)
                            toAdd.artworkUrl60 = obj["artworkUrl60"].asString
                        if (obj["artworkUrl100"] != null)
                            toAdd.artworkUrl100 = obj["artworkUrl100"].asString
                        if (obj["collectionPrice"] != null)
                            toAdd.collectionPrice = obj["collectionPrice"].asDouble
                        if (obj["trackPrice"] != null)
                            toAdd.trackPrice = obj["trackPrice"].asDouble
                        if (obj["releaseDate"] != null)
                            toAdd.releaseDate = obj["releaseDate"].asString
                        if (obj["collectionExplicitness"] != null)
                            toAdd.collectionExplicitness = obj["collectionExplicitness"].asString
                        if (obj["trackExplicitness"] != null)
                            toAdd.trackExplicitness = obj["trackExplicitness"].asString
                        if (obj["discCount"] != null)
                            toAdd.discCount = obj["discCount"].asInt
                        if (obj["discNumber"] != null)
                            toAdd.discNumber = obj["discNumber"].asInt
                        if (obj["trackCount"] != null)
                            toAdd.trackCount = obj["trackCount"].asInt
                        if (obj["trackNumber"] != null)
                            toAdd.trackNumber = obj["trackNumber"].asInt
                        if (obj["trackTimeMillis"] != null)
                            toAdd.trackTimeMillis = obj["trackTimeMillis"].asInt
                        if (obj["country"] != null)
                            toAdd.country = obj["country"].asString
                        if (obj["currency"] != null)
                            toAdd.currency = obj["currency"].asString
                        if (obj["primaryGenreName"] != null)
                            toAdd.primaryGenreName = obj["primaryGenreName"].asString
                        if (obj["contentAdvisoryRating"] != null)
                            toAdd.contentAdvisoryRating = obj["contentAdvisoryRating"].asString
                        if (obj["isStreamable"] != null)
                            toAdd.isStreamable = obj["isStreamable"].asBoolean
                        if (obj["shortDescription"] != null)
                            toAdd.primaryGenreName = obj["shortDescription"].asString
                        if (obj["longDescription"] != null)
                            toAdd.primaryGenreName = obj["longDescription"].asString

                        // add the ITuneResult object in the current result array list
                        toAdd.setHash()
                        searchResultsList.add(toAdd)
                    }
                    snackMessage = "${searchResultsList.size} results found"
                    onSearchListUpdated(true)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // if there was an error in getting the objects:
                // disable the progress bar that show loading data.
                searching_progress_field.visibility = View.GONE
                // Now, maybe search offline results
                searchAndShowOffline(term)
            }
        })
    }

    private fun searchAndShowOffline(term: String) {
        // search for the offline results
        beforeSearchListUpdated()
        LocalDB.search(search_field.text.toString().trim().lowercase(), 1)
        snackMessage = "Searched in offline mode"
    }

    private fun onSearchListUpdated(fromWeb: Boolean = true) {
        runOnUiThread(Runnable {
            Snackbar.make(result_recycler_view, snackMessage, 1500).show()
            // notify the adapter after the list is made
            iTunesResultAdapter.notifyDataSetChanged()
            // After the search results are successfully retrieved, vanish the progress bar
            searching_progress_field.visibility = View.GONE
        })

        // whenever the search list gets update from the web, we have to add data to room database.
        if (fromWeb) {
            LocalDB.insertAll(searchResultsList, 2)
        }

    }

    private fun beforeSearchListUpdated() {
        runOnUiThread(Runnable {
            println("()()()() RUNNING")
            if (searchResultsList.size > 0) {
                result_recycler_view.scrollToPosition(0)
            }
            searchResultsList.clear()
            iTunesResultAdapter.notifyDataSetChanged()
        })
    }

    // Force hides the keyboard.
    private fun hideKeyboard(activity: Activity) {
        val view = activity.findViewById<View>(R.id.search_field)
        if (view != null) {
            val inputMethodManager =
                activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onSearch(result: List<ITuneResults>, id: Int) {
        // copy the list into searchResultList
        for (elem in result) {
            searchResultsList.add(elem)
        }
        snackMessage = "${searchResultsList.size} results found in Offline mode"
        onSearchListUpdated(false)
    }

    override fun onInserted(id: Int) {
        // nothing to do at all till now
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}