package com.kescoode.android.yomo;

import android.test.AndroidTestCase;

/**
 * YomoError类的单元测试
 *
 * @author Kesco Lin
 * @since 2014-12-07
 */
public class YomoErrorTest extends AndroidTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testCode() throws Exception {
        try {
            throwError();
        } catch (YomoError error) {
            assertEquals(123,error.getErrorCode());
        }
    }

    private void throwError() throws YomoError {
        throw new YomoError(123);
    }
}
