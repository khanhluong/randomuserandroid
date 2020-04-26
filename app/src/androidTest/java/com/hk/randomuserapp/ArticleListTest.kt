package com.hk.randomuserapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hk.randomuserapp.feature.MainActivity
import com.hk.randomuserapp.feature.randomuser.view.RandomUserListFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleListTest {
    @get:Rule
    public var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
        mActivityRule.activity.supportFragmentManager.beginTransaction()
            .replace(R.id.layout_root, RandomUserListFragment()).commit()
    }

    @Test
    @Throws(InterruptedException::class)
    fun onLoadRandomUserClick() {
        Espresso.onView(withId(R.id.rv_random_user)).perform(click())
    }
}