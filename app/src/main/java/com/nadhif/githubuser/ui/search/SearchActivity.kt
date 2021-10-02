package com.nadhif.githubuser.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nadhif.githubuser.R
import com.nadhif.githubuser.adapter.UserAdapter
import com.nadhif.githubuser.ui.detail.DetailActivity
import com.nadhif.githubuser.utils.Resource
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    companion object {
        const val QUERY = "query"
    }

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var adapter: UserAdapter
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (savedInstanceState != null) {
            val savedQuery = savedInstanceState.getString(QUERY)
            query = savedQuery
        }

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        adapter = UserAdapter {
            Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_USERNAME, it.login)
                startActivity(this)
            }

        }
        rvUser.adapter = adapter
        rvUser.layoutManager = LinearLayoutManager(this)

        searchViewModel.searchUser.observe(this@SearchActivity, {
            when(it) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    rvUser.visibility = View.GONE
                }
                is Resource.Success -> {
                    adapter.submitList(it.data)
                    progressBar.visibility = View.GONE
                    rvUser.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    Toast.makeText(this@SearchActivity, it.message, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        menu?.findItem(R.id.searchView)?.apply {
            expandActionView()
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    finish()
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    return true
                }
            })
        }


        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        if (query != null){
            searchView.setQuery(query, false)
            searchView.clearFocus()
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newQuery: String): Boolean {
                query = newQuery
                searchViewModel.searchUser(query!!)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY, query)
    }
}