package com.drofff.palindrome.utils;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.exception.ValidationException;
import org.junit.Test;

import static com.drofff.palindrome.utils.StringUtils.randomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EntityUtilsTest {

    @Test
    public void copyNonEditableFieldsTest() {
        String userId = randomString();

        Driver testDriverSource = new Driver();
        testDriverSource.setUserId(userId);

        Driver testDriverDestination = new Driver();

        Driver copyResult = EntityUtils.copyNonEditableFields(testDriverSource, testDriverDestination);

        String resultUserId = copyResult.getUserId();
        assertEquals(userId, resultUserId);
    }

    @Test
    public void notCopyEditableFieldsTest() {
        Driver testDriverSource = new Driver();
        testDriverSource.setFirstName(randomString());

        Driver testDriverDestination = new Driver();
        testDriverDestination.setFirstName(randomString());

        Driver copyResult = EntityUtils.copyNonEditableFields(testDriverSource, testDriverDestination);

        String sourceFirstName = testDriverSource.getFirstName();
        String resultFirstName = copyResult.getFirstName();
        assertNotEquals(sourceFirstName, resultFirstName);
    }

    @Test(expected = ValidationException.class)
    public void copyNonEditableFieldsNullTest() {
        EntityUtils.copyNonEditableFields(null, null);
    }

}
