package com.unloadbrain.blog.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilityTest {

    @Test
    public void shouldReturnCurrentDate() {

        // Given
        Date now = new Date();

        // When
        Date returnedDate = new DateUtility().getCurrentDate();

        // Then
        // Invoke time considered as delta = 10
        assertEquals(now.getTime(), returnedDate.getTime(), 10);

    }
}