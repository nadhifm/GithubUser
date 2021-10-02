package com.nadhif.githubuser.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nadhif.githubuser.R
import com.nadhif.githubuser.adapter.FavoriteUserAdapter
import com.nadhif.githubuser.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel = ViewModelProvider(this, FavoriteViewModel.Factory(activity?.application!!))
            .get(FavoriteViewModel::class.java)

        adapter = FavoriteUserAdapter(
            activity as AppCompatActivity,
            clickListener = {
                Intent(activity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_USERNAME, it.login)
                    startActivity(this)
                }
            },
            deleteSelectedItemListener = {
                favoriteViewModel.deleteSelectedUser(it)
                Toast.makeText(activity, "successfully deleted", Toast.LENGTH_LONG).show()
            }
        )

        rvUser.adapter = adapter
        rvUser.layoutManager = LinearLayoutManager(activity)

        favoriteViewModel.getAllFavoriteUsers()?.observe(viewLifecycleOwner, {
                    adapter.submitList(it)
                    progressBar.visibility = View.GONE

        })
    }
}