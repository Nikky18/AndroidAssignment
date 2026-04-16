package com.nikky.clientassignmnet.utils

import org.junit.Test


class DateFormatterTest {
    @Test
    fun formatTime_shouldReturnCorrectFormattedDate() {
        val timestamp = 1710000000L // sample epoch seconds

        val result = DateFormatter.formatTime(timestamp)

        assert(result.contains(",")) // basic format check
    }

}