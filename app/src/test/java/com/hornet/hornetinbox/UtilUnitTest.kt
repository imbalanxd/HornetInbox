package com.hornet.hornetinbox

import org.junit.Assert
import org.junit.Test

class UtilUnitTest {
    @Test
    fun `parseToLong should work as expected`(){
        Assert.assertEquals(-1, Utils.parseToLong("ssjjsdjdj"))
        Assert.assertEquals(-1, Utils.parseToLong("*&*&*(((("))
        Assert.assertEquals(-1, Utils.parseToLong(" "))

        Assert.assertEquals(1633635725, Utils.parseToLong("1633635725"))
        Assert.assertEquals(1632670025, Utils.parseToLong("1632670025"))
        val currentTime = System.currentTimeMillis()
        Assert.assertEquals(currentTime, Utils.parseToLong("$currentTime"))
    }

    @Test
    fun `generateBackgroundColor should work as expected`() {
        Assert.assertNotNull(Utils.generateBackgroundColor())
        val colorOne = Utils.generateBackgroundColor()
        val colorTwo = Utils.generateBackgroundColor()
        Assert.assertNotEquals(colorOne, colorTwo)
    }

    @Test
    fun `isIncorrectEpochTime should work correctly`() {
        val currentTime = System.currentTimeMillis()
        Assert.assertFalse(Utils.isIncorrectEpochTime(currentTime))
        Assert.assertTrue(Utils.isIncorrectEpochTime(Utils.parseToLong("1633119725")))
        Assert.assertTrue(Utils.isIncorrectEpochTime(Utils.parseToLong("1633011725")))
    }

    @Test
    fun `getRelativeTime should work correctly`() {
        val currentTime = System.currentTimeMillis()
        Assert.assertEquals("moments ago", Utils.getRelativeTime(currentTime).lowercase())
        val fourMinutesInMilliSeconds = 4 * 60 * 1000
        val fourMinutesAgo = currentTime - fourMinutesInMilliSeconds
        Assert.assertEquals("4 minutes ago", Utils.getRelativeTime(fourMinutesAgo).lowercase())
        val incorrectEpochMillisecond = currentTime.div(1000L)
        Assert.assertEquals("moments ago", Utils.getRelativeTime(incorrectEpochMillisecond).lowercase())
    }
}