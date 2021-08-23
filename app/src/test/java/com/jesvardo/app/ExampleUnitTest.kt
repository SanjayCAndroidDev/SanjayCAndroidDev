package com.jesvardo.app

import android.app.Application
import com.jesvardo.app.utils.AppUtils
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {
//    machineuser2@vyzeo.com
//    Vyzeo1!2@3#



    @Mock
    lateinit var mMockContext: Application

    @Mock
    var appUtils: AppUtils = AppUtils(mMockContext)

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testIsEmailValid() {

        val testEmail = "machineuser2@vyzeo.com"
        Assert.assertThat(
            String.format("Email Validity Test failed for %s ", testEmail),
            appUtils.isEmailValid(testEmail),
            `is`(true)
        )
    }
    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(appUtils.isEmailValid("machineuser2@vyzeo.com"))
    }

    @Test
    fun emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(appUtils.isEmailValid("machineuser2@vyzeo"))
    }

    @Test
    fun emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(appUtils.isEmailValid("machineuser2@vyzeo..com"))
    }

    @Test
    fun emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(appUtils.isEmailValid("@vyzeo.com"))
    }

    @Test
    fun emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(appUtils.isEmailValid(""))
    }
}
