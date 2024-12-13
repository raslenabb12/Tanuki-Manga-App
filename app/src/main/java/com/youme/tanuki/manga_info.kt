package com.youme.tanuki

import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MangaInfo : AppCompatActivity() {
    data class MangaItem(val name: String, val img_url: String, val last_chapter: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manga_info_page)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor= Color.parseColor("#222629")
        }

        val mangaName: String? = intent.getStringExtra("Charactername")
        val imagurl: String? = intent.getStringExtra("imageurl")
        val tabLayout = findViewById<TabLayout>(R.id.tf)
        val viewPager = findViewById<ViewPager2>(R.id.aazzes)
        val titlebox=findViewById<TextView>(R.id.textView3)
        val manganame=if (mangaName.toString().length>17){
            "${mangaName?.substring(0,17)}..."
        }else{
            mangaName.toString()
        }
        titlebox.text=manganame
        viewPager.adapter = DramaPagerAdapter(this, mangaName,imagurl)
        viewPager.offscreenPageLimit = 3
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            finish()
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position==0) titlebox.text=manganame
                if (position==1) titlebox.text="Characters"
                if (position==2) titlebox.text="Recommendation"
            }

        })
        tabLayout.tabIconTint = ContextCompat.getColorStateList(this, R.color.gray)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.icon = when (position) {
                0 -> ContextCompat.getDrawable(this, R.drawable.baseline_menu_book_24)
                1 -> ContextCompat.getDrawable(this, R.drawable.baseline_people_outline_24)
                2 -> ContextCompat.getDrawable(this, R.drawable.baseline_recommend_24)
                else -> null
            }
        }.attach()
    }
    private  class DramaPagerAdapter(
        fa: FragmentActivity,
        private val characterName: String?,
        private val imgurl: String?,
    ) : FragmentStateAdapter(fa) {

        private val fragmentReferences = HashMap<Int, Fragment>()
        override fun getItemCount() = 3
        override fun createFragment(position: Int): Fragment {
            val fragment = when (position) {
                0 -> DetailsFragment().apply {
                    arguments = Bundle().apply {
                        putString("Charactername", characterName)
                        putString("imageurl", imgurl)
                    }
                }
                1 -> Characters_frag().apply {
                    arguments = Bundle().apply {
                        putString("Charactername", characterName)
                        putString("imageurl", imgurl)
                    }
                }
                2 -> recommanditon_frag().apply {
                    arguments = Bundle().apply {
                        putString("Charactername", characterName)
                        putString("imageurl", imgurl)
                    }
                }
                else -> throw IllegalStateException("Invalid position $position")
            }
            fragmentReferences[position] = fragment
            return fragment
        }

        fun getFragment(position: Int): Fragment? {
            return fragmentReferences[position]
        }
    }


}
