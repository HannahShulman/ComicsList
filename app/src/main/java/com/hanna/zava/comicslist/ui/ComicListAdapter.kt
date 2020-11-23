package com.hanna.zava.comicslist.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hanna.zava.comicslist.OpenForTesting
import com.hanna.zava.comicslist.R
import com.hanna.zava.comicslist.di.modules.GlideApp
import com.hanna.zava.comicslist.extensions.throttledClickListener
import com.hanna.zava.comicslist.model.Comic

@OpenForTesting
//Prototypes - N/A
//Tests - N/A
class ComicListAdapter(val comicClicked: (id: Int) -> Unit) :
    ListAdapter<Comic, BindableViewHolder<Comic>>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Comic>() {
            override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
                return oldItem.title == newItem.title && oldItem.thumbnail.fullUrl == newItem.thumbnail.fullUrl
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder<Comic> {
        return ComicViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_comic_layout, parent, false),
            comicClicked
        )
    }

    override fun onBindViewHolder(holder: BindableViewHolder<Comic>, position: Int) {
        holder.bindView(currentList[position])

    }
}

class ComicViewHolder(itemView: View, val comicClicked: (id: Int) -> Unit) :
    BindableViewHolder<Comic>(itemView) {
    val comicTextView: TextView = itemView.findViewById(R.id.comic_tv)
    val comicThumbnailView: ImageView = itemView.findViewById(R.id.thumbnail_url)
    override fun bindView(data: Comic) {
        comicTextView.text = data.title
        itemView.throttledClickListener { comicClicked(data.id) }
        GlideApp.with(itemView).load(data.thumbnail.fullUrl).fitCenter().into(comicThumbnailView)
    }
}

abstract class BindableViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bindView(data: T)
    val context: Context get() = itemView.context
}