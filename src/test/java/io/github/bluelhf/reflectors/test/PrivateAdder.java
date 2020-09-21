package io.github.bluelhf.reflectors.test;

import java.util.Arrays;
import java.util.Collection;

public class PrivateAdder {

    private static Collection<Integer> numbers = Arrays.asList(0, 0, 0);

    private static int sum(Collection<Integer> nums) {
        return nums.stream().mapToInt(i -> i).sum();
    }
}
