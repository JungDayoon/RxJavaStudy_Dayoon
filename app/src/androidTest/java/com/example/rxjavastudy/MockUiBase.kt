package com.example.rxjavastudy

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
open class MockUiBase {
    val idlingResource: IdlingResource = SampleIdlingResource()

    val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    val application: MainApplication
        get() = ApplicationProvider.getApplicationContext() as MainApplication

    val currentActivity: Activity
        get() {
            val currentActivityContainer = ArrayList<Activity>(1)
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                if (resumedActivities.iterator().hasNext()) {
                    currentActivityContainer[0] = resumedActivities.iterator().next()
                }
            }
            return currentActivityContainer[0]
        }

    @Before
    open fun before() {
        registerIdlingResource()
    }

    @After
    open fun after() {
        unregisterIdlingResource()
    }

    private fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(idlingResource)

    }

    private fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    fun uiWait(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
