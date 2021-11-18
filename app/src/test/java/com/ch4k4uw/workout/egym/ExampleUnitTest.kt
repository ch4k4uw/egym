package com.ch4k4uw.workout.egym

import com.ch4k4uw.workout.egym.extensions.asUriString
import com.ch4k4uw.workout.egym.extensions.toParcelable
import com.ch4k4uw.workout.egym.login.interaction.UserView
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val user = UserView("1", "2", "3", "4")
        val user64 = user.asUriString
        val user2 = user64.toParcelable<UserView>()
        println(user2)
    }
}