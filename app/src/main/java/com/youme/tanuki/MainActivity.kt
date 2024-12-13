package com.youme.tanuki

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MangaPagingAdapter
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_Tanuki)
        setContentView(R.layout.latest_managa_layout)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        findViewById<ImageButton>(R.id.imageButton5).setOnClickListener {
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color= Color.WHITE
        val headerView = layoutInflater.inflate(R.layout.logl, navigationView, false)
        navigationView.addHeaderView(headerView)
        toggle.syncState()



        val swipeRefreshLayout= findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        val recyclerView: RecyclerView = findViewById(R.id.res)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = MangaPagingAdapter(){mangaItem ->
            val intent = Intent(this@MainActivity, MangaInfo::class.java)
            intent.putExtra("Charactername", mangaItem.title.english?:mangaItem.title.romaji)
            intent.putExtra("imageurl", mangaItem.coverImage.large)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            MangaViewModel().mangaFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                MangaViewModel().mangaFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                    recyclerView.scrollToPosition(0)
                }
            }

            swipeRefreshLayout.isRefreshing=false
        }
    }
}