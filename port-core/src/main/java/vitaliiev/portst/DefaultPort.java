package vitaliiev.portst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DefaultPort implements Port {

    private static final Pattern commaSplitter = Pattern.compile(",");

    private static final Pattern hyphenSplitter = Pattern.compile("-");
    private static final Pattern rangePattern = Pattern.compile("^\\d+-\\d+$");

    private static final Pattern pattern = Pattern.compile("^((\\d+-\\d+|\\d+),)*+(\\d+-\\d+|\\d+)$");

    private int[][] result;


    @Override
    public int[][] parseIndexes(String[] indexes) throws PortException {
        Objects.requireNonNull(indexes);
        int[][] res = new int[indexes.length][];
        for (int i = 0; i < indexes.length; i++) {
            Matcher matcher = pattern.matcher(indexes[i]);
            if (!matcher.matches()) {
                throw new PortException("String \"" + indexes[i] + "\" doesn't match pattern");
            }
            List<Integer> element = new ArrayList<>();
            String[] sequence = commaSplitter.split(indexes[i]);
            for (int j = 0; j < sequence.length; j++) {
                String s = sequence[j];
                if (rangePattern.matcher(s).matches()) {
                    List<Integer> newValues = this.rangeToArray(s);
                    checkSequenceSize(element.size(), newValues.size(), j);
                    element.addAll(newValues);
                } else {
                    checkSequenceSize(element.size(), 1, j);
                    element.add(Integer.parseInt(s));
                }

            }
            res[i] = element.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
        }
        return res;
    }


    @Override
    public int[][] uniqueGroups(String[] indexes) throws PortException {
        Objects.requireNonNull(indexes);
        int[][] resolvedIndexes = this.parseIndexes(indexes);
        long totalSize = Arrays.stream(resolvedIndexes)
                .map(e -> e.length)
                .reduce((a, b) -> a * b)
                .orElseThrow();
        if (totalSize >= Integer.MAX_VALUE) {
            String message = String.format("Produced array length is %d, which exceeds 2^32.", totalSize);
            throw new PortException(message);
        }
        this.result = this.uniqueGroups(this.parseIndexes(indexes), 0);
        return this.result;
    }

    private void checkSequenceSize(int oldSize, int additionalSize, int position) throws PortException {
        if ((long) oldSize + additionalSize >= Integer.MAX_VALUE) {
            String message = String.format("Produced array length is %d, which exceeds 2^32. " +
                            "Stopped parsing on element: index[%d], size \"%d\"",
                    oldSize + additionalSize, position, additionalSize);
            throw new PortException(message);
        }
    }

    /**
     * @param range bounds of range in format Integer-Integer
     * @return list of integers between bounds
     * @throws PortException when <code>range</code> has wrong format
     */
    private List<Integer> rangeToArray(String range) throws PortException {
        if (!rangePattern.matcher(range).matches()) {
            throw new PortException("Expected two numbers and hyphen between them. Got: " + range);
        }
        try {
            List<Integer> bounds = Arrays.stream(hyphenSplitter.split(range))
                    .map(Integer::parseInt)
                    .sorted()
                    .collect(Collectors.toList());
            return IntStream
                    .rangeClosed(bounds.get(0), bounds.get(1))
                    .boxed()
                    .collect(Collectors.toList());
        } catch (NumberFormatException nfe) {
            // can happen if one of bounds exceeds Integer.MAX_VALUE or Integer.MIN_VALUE
            var ex = new PortException("Expected two integers and hyphen between them. Got: " + range);
            ex.initCause(nfe);
            throw ex;
        }
    }

    private int[][] uniqueGroups(int[][] input, int offset) {
        if (offset < 0 || offset >= input.length) {
            throw new IllegalArgumentException("Expected offset from 0 to " + (input.length - 1) + ". Got :" + offset);
        }
        int[][] res;
        if (offset == input.length - 1) {
            res = new int[input[offset].length][input.length - offset];
            for (int i = 0; i < input[offset].length; i++) {
                res[i][0] = input[offset][i];
            }
        } else {
            int[][] intermediateResult = this.uniqueGroups(input, offset + 1);
            int resultLength = input[offset].length * intermediateResult.length;
            int resultRowLength = input.length - offset;
            res = new int[resultLength][resultRowLength];
            for (int i = 0; i < input[offset].length; i++) {
                for (int j = 0; j < intermediateResult.length; j++) {
                    for (int k = 0; k < resultRowLength; k++) {
                        int index = intermediateResult.length * i + j;
                        if (k == 0) {
                            res[index][k] = input[offset][i];
                        } else {
                            res[index][k] = intermediateResult[j][k - 1];
                        }
                    }
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "DefaultPort{" +
                "result=" + Arrays.stream(this.result)
                .map(Arrays::toString)
                .collect(Collectors.toList()) +'}';
    }
}
