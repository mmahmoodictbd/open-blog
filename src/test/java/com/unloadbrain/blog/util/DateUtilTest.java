package com.unloadbrain.blog.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilTest {

    @Test
    public void shouldReturnCurrentDate() {

        // Given
        Date now = new Date();

        // When
        Date returnedDate = new DateUtil().getCurrentDate();

        // Then
        // Invoke time considered as delta = 10
        assertEquals(now.getTime(), returnedDate.getTime(), 10);

    }
}