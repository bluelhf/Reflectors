package io.github.bluelhf.reflectors.test;

import io.github.bluelhf.reflectors.Reflectors;
import io.github.bluelhf.reflectors.statics.ClassReference;
import io.github.bluelhf.reflectors.util.Mappers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectorsDemo {
    public static void main(String[] args) {
        ClassReference ref = Reflectors.refer("io.github.bluelhf.reflectors.test.PrivateAdder");

        List<Integer> numbers = Arrays.asList(592, 2394, 129);
        ref.field("numbers").ifPresent(bfr -> {
            System.out.println("Previous 'numbers' field: " + bfr.get().resultOr("null"));
            bfr.set(numbers);
            System.out.println("New 'numbers' field: " + bfr.get().resultOr("null"));
        });
        ref.method("sum", Collection.class).ifPresent(bmr -> bmr.invoke(numbers).map(Mappers.castTo(Integer.class)).consume((num) -> {
            System.out.println(numbers.stream().map(i -> "" + i).collect(Collectors.joining(" + ")) + " = " + num);
        }, Throwable::printStackTrace));

        Reflectors.tellMeWhatsWrongPlease();
    }
}
