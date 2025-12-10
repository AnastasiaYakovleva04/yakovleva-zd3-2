package com.companyname.yakovleva_zd3_2

import android.app.Activity
import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class QuestsActivity : Activity() {

    private val apiKey = "ceb197dd"
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: MovieAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var genreSpinner: Spinner
    private val allMovies = ArrayList<Movie>()
    private val genres = arrayOf(
        "Movie",
        "Action",
        "Comedy",
        "Drama",
        "Horror",
        "Romance",
        "Adventure"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quests)


        genreSpinner = findViewById(R.id.genre_spinner)
        recyclerView = findViewById(R.id.recycler)
        searchView = findViewById(R.id.search)
        progressBar = findViewById(R.id.progress)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = MovieAdapter { movie ->
            openMovieDetails(movie)
        }
        recyclerView.adapter = adapter
        setupSpinner()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchMovies(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.length >= 3) {
                        searchMovies(it)
                    } else if (it.isEmpty()) {
                        adapter.data = allMovies
                    }
                }
                return false
            }
        })

        loadInitialMovies("movie")
    }

    private fun loadInitialMovies(genre: String) {
        searchMovies(genre)
    }

    private fun searchMovies(query: String) {
        if (query.isEmpty()) return

        progressBar.visibility = View.VISIBLE

        val url = "https://www.omdbapi.com/?s=$query&apikey=$apiKey"

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                try {
                    val obj = JSONObject(response)
                    if (obj.getString("Response") == "True") {
                        val moviesArray = obj.getJSONArray("Search")
                        val tempMovies = ArrayList<Movie>()
                        for (i in 0 until moviesArray.length()) {
                            val movieJson = moviesArray.getJSONObject(i)
                            val imdbID = movieJson.getString("imdbID")
                            loadMovieDetails(imdbID) { movie ->
                                movie?.let {
                                    tempMovies.add(it)
                                    if (tempMovies.size == moviesArray.length()) {
                                        allMovies.clear()
                                        allMovies.addAll(tempMovies)
                                        adapter.data = tempMovies
                                        progressBar.visibility = View.GONE
                                    }
                                }
                            }
                        }
                        if (moviesArray.length() == 0) {
                            adapter.data = emptyList()
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Фильмы не найдены", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        adapter.data = emptyList()
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Фильмы не найдены", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Ошибка парсинга", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Ошибка сети: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(stringRequest)
    }

    private fun loadMovieDetails(imdbID: String, callback: (Movie?) -> Unit) {
        val url = "https://www.omdbapi.com/?apikey=$apiKey&i=$imdbID"

        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                try {
                    val movieObj = JSONObject(response)

                    val genresString = movieObj.getString("Genre")
                    val genresList = if (genresString != "N/A") {
                        genresString.split(", ").toList()
                    } else {
                        listOf("Не указано")
                    }

                    val movie = Movie(
                        Title = movieObj.getString("Title"),
                        Poster = movieObj.getString("Poster"),
                        Genre = genresList,
                        Plot = movieObj.getString("Plot"),
                        Year = movieObj.getString("Year"),
                        Runtime = movieObj.getString("Runtime"),
                        imdbID = imdbID
                    )

                    callback(movie)
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(null)
                }
            },
            { error ->
                callback(null)
            }
        )

        queue.add(stringRequest)
    }

    private fun openMovieDetails(movie: Movie) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(movie.Title)
            .setMessage("""
            Год: ${movie.Year}
            Длительность: ${movie.Runtime}
            Жанры: ${movie.Genre.joinToString(", ")}
            Описание: ${movie.Plot}
        """.trimIndent())
            .setPositiveButton("OK", null)
            .create()

        dialog.show()
    }

    private fun setupSpinner(){
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, genres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = adapter

        genreSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var selectedGenre: Any
                if (position  == 0)
                    selectedGenre = "movie"
                else
                    genres[position].toLowerCase()
                val genre = genres[position]
                loadInitialMovies(genre)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

}