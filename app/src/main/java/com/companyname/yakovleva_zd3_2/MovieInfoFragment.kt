package com.companyname.yakovleva_zd3_2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso

class MovieInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_info, container, false)

        val title = arguments?.getString("title") ?: ""
        val poster = arguments?.getString("poster") ?: ""
        val genreList = arguments?.getStringArrayList("genres") ?: arrayListOf()
        val plot = arguments?.getString("plot") ?: ""
        val year = arguments?.getString("year") ?: ""
        val runtime = arguments?.getString("runtime") ?: ""

        val name: TextView = view.findViewById(R.id.film_name)
        val posterImg: ImageView = view.findViewById(R.id.poster)
        val genre: TextView = view.findViewById(R.id.genres_film)
        val yearText: TextView = view.findViewById(R.id.year_film)
        val runtimeText: TextView = view.findViewById(R.id.runtime_film)
        val plotText: TextView = view.findViewById(R.id.plot_film)

        name.text = title
        Picasso.get()
            .load(poster)
            .into(posterImg)
        genre.text = "Жанры: " + genreList.joinToString(", ")
        yearText.text = "Год: $year"
        runtimeText.text = "Длительность: $runtime"
        plotText.text = "Описание: $plot"

        return view
    }
}