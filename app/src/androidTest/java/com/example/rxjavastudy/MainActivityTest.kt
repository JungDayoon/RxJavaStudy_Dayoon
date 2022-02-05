package com.example.rxjavastudy

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import com.example.rxjavastudy.ui.MainActivity
import org.junit.Test

class MainActivityTest: MockUiBase() {
    @Test
    fun main_activity() {
        val intent = Intent(targetContext, MainActivity::class.java)
        val activityScenario = ActivityScenario.launch<MainActivity>(intent)
        uiWait(3000)
        activityScenario.close()
    }
}
