package com.example.spacenews.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: Int,
    val title: String,
    val authors: List<Author>,
    val url: String,
    val image_url: String,
    val news_site: String,
    val summary: String,
    val published_at: String,
    val updated_at: String,
    val featured: Boolean,
    //val launches: List<Launch>,
   // val events: List<Event>
):Parcelable
@Parcelize
data class Author(
    val name: String
):Parcelable