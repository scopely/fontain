package com.scopely.fontain.test;

import com.scopely.fontain.utils.FontViewUtils;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class CapitalizationTest extends TestCase {
    private final String expectedOutput;
    private final String input;
    private final int capsMode;

    public CapitalizationTest(int capsMode, String input, String expectedOutput) {
        this.capsMode = capsMode;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {FontViewUtils.CAPS_MODE_CHARACTERS, "hello", "HELLO"},
                {FontViewUtils.CAPS_MODE_CHARACTERS, "Hello", "HELLO"},
                {FontViewUtils.CAPS_MODE_CHARACTERS, "hello, and good day", "HELLO, AND GOOD DAY"},
                {FontViewUtils.CAPS_MODE_WORDS, "hello", "Hello"},
                {FontViewUtils.CAPS_MODE_WORDS, "hello, and good day", "Hello, And Good Day"},
                {FontViewUtils.CAPS_MODE_WORDS, "hello Mr. Smith", "Hello Mr. Smith"},
                {FontViewUtils.CAPS_MODE_WORDS, "hello Mr. McArdle", "Hello Mr. McArdle"},
                {FontViewUtils.CAPS_MODE_SENTENCES, "hello", "Hello"},
                {FontViewUtils.CAPS_MODE_SENTENCES, "hello. how are you?", "Hello. How are you?"},
                {FontViewUtils.CAPS_MODE_SENTENCES, "hello.  how are you?", "Hello.  How are you?"},
                {FontViewUtils.CAPS_MODE_SENTENCES, "hello; good day", "Hello; good day"},
        });
    }


    @Test
    public void testCapitalization() throws Exception {
        assertEquals(expectedOutput, FontViewUtils.capitalizeCharSequence(input, capsMode));
    }
}
