package cz.vcelnicerudna

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import cz.vcelnicerudna.adapters.NewsAdapter
import cz.vcelnicerudna.interfaces.VcelniceAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        super.actionBarToggleWithNavigation(this)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NewsAdapter(arrayOf())

        recyclerView = findViewById<RecyclerView>(R.id.news_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
        loadNews()
    }

    private fun loadNews() {
        vcelniceAPI.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            result ->
                                Log.d("NewsActivity", "Result: ${result.size}")
                                viewAdapter.loadNewData(result)
                        },
                        {
                            error ->
                            Log.d("NewsActivity", "error " + error.message)
                        }
                )
    }
}
