package vitaliiev.portst;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPortTest {

    String[] testCase1 = new String[] {"1,3-5", "2", "3-4"};

    int[][] parseResult1 = new int[][] {{1, 3, 4, 5}, {2}, {3,4}};
    int[][] result1 = new int[][] {{1, 2, 3}, {1, 2, 4}, {3, 2, 3}, {3, 2, 4}, {4, 2, 3}, {4, 2, 4}, {5, 2, 3}, {5, 2, 4}};

    @Test
    void testParseIndexes() {
        assertArrayEquals(parseResult1, new DefaultPort().parseIndexes(testCase1));
    }

    @Test
    void testParseReverseOrder() {
        Port port = new DefaultPort();
        String[] indexes = new String[] {"1-100"};
        String[] indexesReversed = new String[] {"100-1"};
        assertEquals(port.parseIndexes(indexesReversed).length, port.parseIndexes(indexes).length);
    }

    @Test
    void testParseEmptyIndex() {
        Port port = new DefaultPort();
        assertArrayEquals(new int[][] {}, port.parseIndexes(new String[] {}));
    }

    @Test
    void testParseEmptyStrings() {
        Port port = new DefaultPort();
        assertThrows(PortException.class, () -> port.parseIndexes(new String[] {"1,3-5", "", "3-4"}));
        assertThrows(PortException.class, () -> port.parseIndexes(new String[] {"", "", ""}));
        assertThrows(PortException.class, () -> port.parseIndexes(new String[] {""}));
        assertThrows(PortException.class, () -> port.parseIndexes(new String[] {"", "1,3-5", "3", "3-4", ""}));
    }

    @Test
    void testParseMalformedStrings() {
        Port port = new DefaultPort();
        assertThrows(PortException.class, () -> port.parseIndexes(new String[] {"1,3-dfsd5", "2", "3-4"}));
        assertThrows(PortException.class, () -> port.parseIndexes(new String[] {"1,3---7", "2", "3-4"}));
        assertThrows(PortException.class, () -> port.parseIndexes(new String[] {"1,,3-7", "2", "3-4"}));
    }

    @Test
    void testParseNegative() {
        Port port = new DefaultPort();
        String[] indexes = new String[] {"-100-1"};
        assertThrows(PortException.class, () -> port.parseIndexes(indexes));
    }
// TODO: 15.04.2023 fix out of memory error
//    @Test
//    void testParseBigRange() {
//        Port port = new DefaultPort();
//        String bigRange = String.format("1-%1$d,100-%1$d,100-%1$d,100-%1$d", Integer.MAX_VALUE);
//        String[] indexes = new String[] {bigRange};
//        assertThrows(PortException.class, () -> port.parseIndexes(indexes));
//    }

// TODO: 15.04.2023 fix out of memory error
//    @Test
//    void testParseBigRanges() {
//        Port port = new DefaultPort();
//        String bigRange = String.format("1-%d", Integer.MAX_VALUE);
//        String[] indexes = new String[] {bigRange,bigRange,bigRange};
//        assertEquals(port.parseIndexes(indexes)[0].length, indexes.length);
//    }

    @Test
    void testUniqueGroups() {
        assertArrayEquals(result1, new DefaultPort().uniqueGroups(testCase1));
    }

// TODO: 15.04.2023 fix out of memory error
//    @Test
//    void testUniqueGroupsBigRanges() {
//        Port port = new DefaultPort();
//        String bigRange = String.format("1-%d", Integer.MAX_VALUE);
//        String[] indexes = new String[] {bigRange,bigRange,bigRange};
//        assertThrows(PortException.class, () -> port.parseIndexes(indexes));
//    }
}