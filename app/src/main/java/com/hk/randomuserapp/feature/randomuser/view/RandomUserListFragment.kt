package com.hk.randomuserapp.feature.randomuser.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hk.randomuserapp.R
import com.hk.randomuserapp.data.local.entity.RandomUserEntity
import com.hk.randomuserapp.feature.MainActivity
import com.hk.randomuserapp.feature.base.BaseFragment
import com.hk.randomuserapp.feature.dialog.FilterUserDialog
import com.hk.randomuserapp.feature.randomuser.adapter.RandomUserRecycleViewAdapter
import com.hk.randomuserapp.feature.randomuser.viewmodel.RandomUserListViewModel
import kotlinx.android.synthetic.main.fragment_list_random_user.*
import kotlinx.android.synthetic.main.item_loading.*
import timber.log.Timber
import javax.inject.Inject

class RandomUserListFragment : BaseFragment(), FilterUserDialog.FilterDialogListener {

    companion object {
        fun newInstance(): RandomUserListFragment {
            return RandomUserListFragment()
        }
    }

    private val MALE = "male"
    private var isLoading = false
    private var page = 1
    private val listResult = mutableListOf<RandomUserEntity>()

    lateinit var adapter: RandomUserRecycleViewAdapter

    @Inject
    lateinit var getRandomUserListViewModel: RandomUserListViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).getRandomUserComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_list_random_user
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        page = 1
        getRandomUserListViewModel.getListRandomUserLiveData(page)
            .observe(viewLifecycleOwner, Observer { result ->
                result.data?.let { listResult.addAll(it) }
                val layoutManager = LinearLayoutManager(context)
                adapter = RandomUserRecycleViewAdapter(
                        context!!,
                        listResult
                    )
                rv_random_user.layoutManager = layoutManager
                rv_random_user.adapter = adapter
                page++
                progressBar.visibility = View.GONE
            })

        initScroll()

        btn_filter.setOnClickListener{
            showFilterUser()
        }
    }


    private fun initScroll(){
        rv_random_user.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (linearLayoutManager != null) {
                    val visibleItemCount = recyclerView.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
                    val lastItem: Int = firstVisibleItem.plus(visibleItemCount)
                    if (lastItem == totalItemCount && totalItemCount != 0) {
                        // check last item. if scroll to end of list, we will start
                        // load more.
                        val v = recyclerView.getChildAt(visibleItemCount - 1)
                        if (v?.bottom!! <= recyclerView.bottom && !isLoading) {
                            loadMoreItem(adapter)
                            isLoading = true
                        }
                    }
                }
            }
        })
    }

    fun loadMoreItem(randomUserRecycleViewAdapter: RandomUserRecycleViewAdapter){
        adapter.setShowLoading(true)
        getRandomUserListViewModel.getListRandomUserLiveData(page)
            .observe(viewLifecycleOwner, Observer { result ->
                Timber.d("load more items at page $page")
                adapter.setShowLoading(false)
                result.data?.let { listResult.addAll(it) }
                Timber.d("item ${listResult.size}")
                randomUserRecycleViewAdapter.notifyItemInserted(listResult.size)
                isLoading = false
                page++
            })
    }

    private fun showFilterUser() {
        val fm = childFragmentManager
        val dialogFragment = FilterUserDialog()
        dialogFragment.show(fm, "filter")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = SearchView((context as MainActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(context, "query: $query", Toast.LENGTH_LONG).show()
                getRandomUserListViewModel.filterRandomUserData(adapter = adapter, query = query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                getRandomUserListViewModel.filterRandomUserData(adapter = adapter, query = newText)
                return false
            }
        })
        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                getRandomUserListViewModel.resetDataFilter(adapter = adapter)
            }

        })
    }

    override fun onChooseFilter(isMale: Boolean, dialogFragment: DialogFragment) {
        Timber.d("onChooseFilter")
        if(isMale){
            getRandomUserListViewModel.filterRandomUserData(adapter, MALE)
        }else{
            getRandomUserListViewModel.resetDataFilter(adapter)
        }
    }
}