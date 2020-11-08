package io.github.bluelhf.reflectors.test;

import java.util.Arrays;
import java.util.Collection;

public class PrivateAdderInstance {


    private final Collection<Integer> numbers = Arrays.asList(0, 0, 0);

    private int sum() {
        return numbers.stream().mapToInt(i -> i).sum();
    }
}
