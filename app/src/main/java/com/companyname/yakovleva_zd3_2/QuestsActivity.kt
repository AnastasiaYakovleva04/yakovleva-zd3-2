package com.companyname.yakovleva_zd3_2

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class QuestsActivity : Activity() {

    private val apiKey = "ceb197dd"
    private lateinit var rec: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quests)
        rec = findViewById(R.id.recycler)
        rec.layoutManager = GridLayoutManager(this, 3)
        rec.adapter = QuestRecycler(this, Quests.myObj().list)

        loadMovies()
    }

    private fun loadMovies() {
        Thread {
            try {
                val url = URL("https://www.omdbapi.com/?s=movie&apikey=$apiKey")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                connection.disconnect()

                val jsonObject = JSONObject(response)
                val moviesArray = jsonObject.getJSONArray("Search")

                val movies = mutableListOf<Movie>()
                for (i in 0 until moviesArray.length()) {
                    val movieJson = moviesArray.getJSONObject(i)
                    val title = movieJson.getString("Title")
                    val poster = movieJson.getString("Poster")
                    val genre = movieJson.getString("Genre").split(", ")
                    movies.add(Movie(title, poster, genre))
                }

                runOnUiThread {
                    showMovies(movies)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    //error
                }
            }
        }.start()
    }

    private fun showMovies(movies: List<Movie>) {
        val inflater = LayoutInflater.from(this)

        for (movie in movies) {
            val movieView = inflater.inflate(R.layout.quest_adapter, rec, false)

            val imageView = movieView.findViewById<ImageView>(R.id.image_quests)
            val titleView = movieView.findViewById<TextView>(R.id.title_quests)
            val genre = movieView.findViewById<TextView>(R.id.descr)


            titleView.text = movie.Title
            genre.text = movie.Genre.toString()

            Picasso.get()
                .load(movie.Poster)
                .placeholder(R.drawable.bg1)
                .into(imageView)
            rec.addView(movieView)
        }
    }
}