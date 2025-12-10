package com.companyname.yakovleva_zd3_2
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieAdapter(private val onItemClick: (Movie) -> Unit):
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var data: List<Movie> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = data[position]

        Picasso.get()
            .load(movie.Poster)
            .placeholder(R.drawable.bg1)
            .into(holder.moviePoster)

        holder.movieName.text = movie.Title
        holder.movieMore.text = "Подробнее"

        holder.movieGenres.text = "Жанры: " + movie.Genre.joinToString(", ")
        holder.movieMore.setOnClickListener {
            onItemClick(movie)
        }
        holder.itemView.setOnClickListener {
            onItemClick(movie)
        }
    }

    override fun getItemCount(): Int = data.size

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val movieName: TextView = itemView.findViewById(R.id.name)
        val moviePoster: ImageView = itemView.findViewById(R.id.poster)
        val movieGenres: TextView = itemView.findViewById(R.id.genres)
        val movieMore: TextView = itemView.findViewById(R.id.more)
    }
}