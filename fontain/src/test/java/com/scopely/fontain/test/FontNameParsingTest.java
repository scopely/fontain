package com.scopely.fontain.test;

import com.scopely.fontain.enums.Slope;
import com.scopely.fontain.enums.Weight;
import com.scopely.fontain.enums.Width;
import com.scopely.fontain.utils.ParseUtils;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class FontNameParsingTest extends TestCase {
    private String name;
    private int expectedWidth;
    private int expectedWeight;
    private boolean expectedSlope;

    public FontNameParsingTest(String name, int expectedWidth, int expectedWeight, boolean expectedSlope) {
        this.name = name;
        this.expectedWidth = expectedWidth;
        this.expectedWeight = expectedWeight;
        this.expectedSlope = expectedSlope;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Helvetica", Width.NORMAL.value, Weight.NORMAL.value, Slope.NORMAL.value},
                {"HelveticaBold", Width.NORMAL.value, Weight.BOLD.value, Slope.NORMAL.value},
                {"HelveticaBoldItalic", Width.NORMAL.value, Weight.BOLD.value, Slope.ITALIC.value},
                {"Helvetica-Bold-Italic", Width.NORMAL.value, Weight.BOLD.value, Slope.ITALIC.value},
                {"HelveticaNarrowExtraBold", Width.NARROW.value, Weight.EXTRA_BOLD.value, Slope.NORMAL.value},
                {"HelveticaExtraNarrowBold", Width.EXTRA_NARROW.value, Weight.BOLD.value, Slope.NORMAL.value},
                {"Helvetica_Extra_Narrow_Bold", Width.EXTRA_NARROW.value, Weight.BOLD.value, Slope.NORMAL.value},
                {"Helvetica.Ultra.Narrow.Bold", Width.ULTRA_NARROW.value, Weight.BOLD.value, Slope.NORMAL.value},
                {"Helvetica.Ultra.Narrow.870", Width.ULTRA_NARROW.value, 870, Slope.NORMAL.value},
        });
    }


    @Test
    public void testWidthParsing() throws Exception {
        assertEquals(name, expectedWidth, ParseUtils.parseWidth(name));
    }

    @Test
    public void testWeightParsing() throws Exception {
        assertEquals(name, expectedWeight, ParseUtils.parseWeight(name));
    }

    @Test
    public void testSlopeParsing() throws Exception {
        assertEquals(name, expectedSlope, ParseUtils.parseItalics(name));
    }
}
