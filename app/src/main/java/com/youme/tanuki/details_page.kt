package com.youme.tanuki

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import kotlin.time.Duration.Companion.seconds

class DetailsFragment : Fragment(R.layout.info_page) {
    private var mangaName: String? = null
    private var imgurl: String? = null
    private val viewModel: MangaViewModel2 by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mangaName = arguments?.getString("Charactername")
        imgurl = arguments?.getString("imageurl")

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(imgurl)
            .into(view.findViewById<ImageView>(R.id.imageView3))
        Glide.with(requireContext()).load(imgurl)
            .into(view.findViewById<ImageView>(R.id.imageView4))
        view.findViewById<TextView>(R.id.textView4).text = mangaName?.trim()
        viewModel.fetchMangaDetails(mangaName.toString())
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mangaDetails.collect { details ->
                details?.let { manga ->
                    view.findViewById<TextView>(R.id.textView5).text=manga.description?:"No description"
                    view.findViewById<TextView>(R.id.textView6).text=manga.startDate?.year.toString()+" - "+manga.endDate?.year.toString()
                    view.findViewById<TextView>(R.id.textView107).text=(manga.averageScore ?: 0).toString()
                    view.findViewById<TextView>(R.id.textView102).text=" "+manga.status
                    view.findViewById<TextView>(R.id.textView104).text="#"+manga.popularity.toString()
                    view.findViewById<RelativeLayout>(R.id.loading).visibility=View.GONE
                    val banner = manga.bannerImage?:manga.coverImage?.large
                    Glide.with(requireContext()).load(banner)
                        .into(view.findViewById<ImageView>(R.id.imageView4))
                    view.findViewById<ScrollView>(R.id.main).visibility=View.VISIBLE
                    manga.genres?.forEach {
                        tagss(view,it)
                    }
                }
            }
        }


    }
    private suspend fun tagss(view: View, tag:String){
        withContext(Dispatchers.Main){
            val inflater = LayoutInflater.from(context)
            val itemView = inflater.inflate(R.layout.genre, null)
            val container = view.findViewById<LinearLayout>(R.id.tags)
            itemView.findViewById<TextView>(R.id.textView11).text=tag
            container.addView(itemView)
        }
    }
}