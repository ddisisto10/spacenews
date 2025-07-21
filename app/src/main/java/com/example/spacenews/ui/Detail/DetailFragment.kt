package com.example.spacenews.ui.Detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.spacenews.databinding.FragmentDetailBinding
import com.example.spacenews.data.model.Article
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailFragment:Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var currentArticle: Article? = null

    private var scrollPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentArticle == null) {
            val args = DetailFragmentArgs.fromBundle(requireArguments())
            currentArticle = args.article
        }

        // Cargar los detalles del artículo
        currentArticle?.let { loadDetails(it) }

        // Restaurar la posición del scroll si existe en el savedInstanceState
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION, 0)
            binding.root.post {
                binding.detailScrollView.scrollTo(0, scrollPosition)
            }
        }
    }

    private fun loadDetails(article: Article) {
        binding.newsImageView.load(article.image_url) {
            error(android.R.drawable.ic_menu_report_image)
            listener(
                onStart = { Log.d("Coil", "Image load started") },
                onSuccess = { _, _ -> Log.d("Coil", "Image load success") },
                onError = { _, _ -> Log.e("Coil", "Image load error") }
            )
        }

        binding.titleTextView.text = article.title
        binding.summaryTextView.text = article.summary
        binding.newsSiteTextView.text = "Fuente: ${article.news_site}"

        // Formatea los autores
        val authorNames = article.authors.joinToString(", ") { it.name }
        binding.authorTextView.text = if (authorNames.isNotEmpty()) "Por: $authorNames" else "Autor Desconocido"

        // Formatea la fecha
        val formattedDate = try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val date = parser.parse(article.published_at)
            SimpleDateFormat("dd MMMM, yyyy HH:mm", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            article.published_at
        }
        binding.publishedDateTextView.text = "Publicado el: $formattedDate"
    }

    // Guardar el estado cuando se destruye el fragment
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Guardar la posición actual del scroll
        scrollPosition = binding.detailScrollView.scrollY
        outState.putInt(KEY_SCROLL_POSITION, scrollPosition)
    }

    // Liberar recursos cuando se destruye la vista
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_SCROLL_POSITION = "scroll_position"
    }
}