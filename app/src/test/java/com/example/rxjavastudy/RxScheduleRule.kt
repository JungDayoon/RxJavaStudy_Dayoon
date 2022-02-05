package com.example.rxjavastudy

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { SCHEDULER_INSTANCE }
                RxJavaPlugins.setComputationSchedulerHandler { SCHEDULER_INSTANCE }
                RxJavaPlugins.setNewThreadSchedulerHandler { SCHEDULER_INSTANCE }
                RxJavaPlugins.setSingleSchedulerHandler { SCHEDULER_INSTANCE }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { SCHEDULER_INSTANCE }
                try {
                    base?.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }

    companion object {
        private val SCHEDULER_INSTANCE = Schedulers.trampoline()
    }
}
