package com.hk.randomuserapp.feature.randomuserdetail.view

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.hk.randomuserapp.R
import com.hk.randomuserapp.feature.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_random_user_detail.*

class RandomUserDetailFragment : BaseFragment(){

    override fun getLayoutId(): Int {
        return R.layout.fragment_random_user_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val imageUrl = args?.getString("image_url", "")
        val fullName = args?.getString("full_name", "")
        tv_full_name.text = fullName
        Glide.with(view).load(imageUrl!!).into(imv_user_detail)
    }
}