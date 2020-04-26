package com.hk.randomuserapp.feature.randomuser.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hk.randomuserapp.R
import com.hk.randomuserapp.data.local.entity.RandomUserEntity
import com.hk.randomuserapp.feature.base.BaseViewHolder
import com.hk.randomuserapp.feature.randomuserdetail.view.RandomUserDetailFragment
import timber.log.Timber
import java.util.*


open class RandomUserRecycleViewAdapter() : RecyclerView.Adapter<BaseViewHolder<*>>(), Filterable {

    private lateinit var results: List<RandomUserEntity>
    private  lateinit var context: Context
    private lateinit var originalResult: List<RandomUserEntity>

    private val VIEW_ITEM = 1
    private val VIEW_LOADING = 0

    /**
     * set is show footer loading.
     */
    private var isShowLoading = false

    constructor(context: Context, results: List<RandomUserEntity>) : this(){
        this.results = results
        this.context = context
        originalResult = results
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return if(viewType == VIEW_ITEM){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_random_user, parent, false)
            ItemViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            ItemLoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val result: RandomUserEntity = results[position]

        when (holder) {
            is ItemViewHolder -> holder.bind(result)
            is ItemLoadingViewHolder -> holder.bind(result)
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            val activity = holder.itemView.context as AppCompatActivity
            val randomUserDetailFragment: Fragment = RandomUserDetailFragment()

            val args = Bundle()
            args.putString("image_url", result.picUrl)
            args.putString("full_name", result.firstName +  " " + result.lastName)
            randomUserDetailFragment.arguments = args

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.layout_root, randomUserDetailFragment).addToBackStack("detail").commit()
        })

    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionLoading(position)) VIEW_LOADING else VIEW_ITEM
    }

    override fun getItemCount(): Int {
        return results.size + getItemLoading()
    }

    private fun isPositionLoading(position: Int): Boolean {
        val itemCount: Int = results.size
        return isShowLoading && position == itemCount;
    }

    private fun getItemLoading(): Int {
        return if (isShowLoading) {
            1
        } else 0
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {

                val queryString = charSequence?.toString()?.toLowerCase()

                Timber.d("Query string: %s", queryString)

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    results = originalResult
                else
                    results.filter {
                        it.gender.toLowerCase(Locale.getDefault()).contentEquals(charSequence)
                                || it.firstName.toLowerCase(Locale.getDefault()).contains(charSequence)
                                || it.lastName.toLowerCase(Locale.getDefault()).contains(charSequence)
                    }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                results = if(filterResults?.values is Unit){
                    //not update
                    originalResult
                }else {
                    filterResults?.values as List<RandomUserEntity>
                }
                notifyDataSetChanged()
            }

        }
    }

    fun resetFilter(){
        results = originalResult
        notifyDataSetChanged()
    }

    fun setShowLoading(isShow: Boolean) {
        if (isShow == isShowLoading) {
            return
        }
        isShowLoading = isShow
        if (isShow) {
            notifyItemInserted(itemCount)
        } else {
            notifyItemRemoved(itemCount)
        }
    }
}


class ItemViewHolder(itemView: View) : BaseViewHolder<RandomUserEntity>(itemView) {

    private var mTvGender: TextView = itemView.findViewById(R.id.tv_gender)
    private var mImgThumbnail: ImageView = itemView.findViewById(R.id.img_thumb)
    private var mTvDob: TextView = itemView.findViewById(R.id.tv_dob)
    private var mTvTitleName: TextView = itemView.findViewById(R.id.tv_title_name)

    override fun bind(item: RandomUserEntity){
        mTvGender.text = item.gender
        Glide.with(itemView).load(item.thumbnailUrl).into(mImgThumbnail)
        mTvDob.text = item.dob
        mTvTitleName.text = item.firstName.plus(" ").plus(item.lastName)

    }
}

class ItemLoadingViewHolder(itemView: View) : BaseViewHolder<RandomUserEntity>(itemView) {
    override fun bind(item: RandomUserEntity) {
    }
}