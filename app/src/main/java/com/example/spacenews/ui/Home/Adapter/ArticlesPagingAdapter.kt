package com.example.space.ui.Home.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.intl.Locale
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.spacenews.databinding.ArticlesViewHolderBinding
import com.example.spacenews.data.model.Article
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class ArticlesPagingAdapter(
    private val onItemClick: (Article) -> Unit
) : PagingDataAdapter<Article, ArticlesPagingAdapter.ViewHolder>(ArticleDiffCallback()) {
    
    private val outputDateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.current.platformLocale)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ArticlesViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position) ?: return
        with(holder.binding) {
            if (article.image_url.isNotEmpty()) {
                imageViewNews.load(article.image_url) {
                    size(300, 200)
                    error(android.R.drawable.ic_menu_report_image)
                }
            } else {
                imageViewNews.visibility = View.GONE
            }

            textViewTitle.text = article.title
            textViewSummary.text = article.summary
            textViewNewsSite.text = article.news_site

            // Format y mostrar la fecha
            try {
                val parsedDate = OffsetDateTime.parse(article.published_at)
                textViewPublishedAt.text = parsedDate.format(outputDateFormatter)
                textViewPublishedAt.visibility = View.VISIBLE
            } catch (e: Exception) {
                // En caso de error de parseo, muestra la fecha original o un mensaje de error
                textViewPublishedAt.text = article.published_at // O podrías ocultarlo
                if (article.published_at.isNullOrEmpty()) {
                    textViewPublishedAt.visibility = View.GONE
                } else {
                    textViewPublishedAt.visibility = View.VISIBLE
                }
            }

            // Mostrar el primer autor, si existe
            if (!article.authors.isNullOrEmpty() && article.authors[0].name.isNotEmpty()) {
                textViewAuthor.text = "By ${article.authors[0].name}"
                textViewAuthor.visibility = View.VISIBLE
            } else {
                textViewAuthor.visibility = View.GONE // Ocultar si no hay autor
            }

            root.setOnClickListener {
                onItemClick(article)
            }
        }
    }

    inner class ViewHolder(val binding: ArticlesViewHolderBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root)
}

class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
