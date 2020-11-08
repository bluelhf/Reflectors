package io.github.bluelhf.reflectors.test;

import io.github.bluelhf.reflectors.Reflectors;
import io.github.bluelhf.reflectors.dynamics.InstanceReference;
import io.github.bluelhf.reflectors.statics.ClassReference;
import io.github.bluelhf.reflectors.util.Mappers;

import java.util.Arrays;
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
        ref.method("sm").ifPresent(bmr -> bmr.invoke().map(Mappers.castTo(Integer.class)).consume((num) -> {
            System.out.println(numbers.stream().map(i -> "" + i).collect(Collectors.joining(" + ")) + " = " + num);
        }, Throwable::printStackTrace));


        PrivateAdderInstance inst = new PrivateAdderInstance();
        InstanceReference instRef = Reflectors.reflect(inst);

        instRef.field("numbers").ifPresent(ifr -> {
            System.out.println("Previous 'numbers' field: " + ifr.get().resultOr("null"));
            ifr.set(numbers);
            System.out.println("New 'numbers' field: " + ifr.get().resultOr("null"));
        });

        instRef.method("sum").ifPresent(imr -> {
            System.out.println("Calling 'sum' method");
            System.out.println("sum() resulted in: " + imr.invoke().resultOr("null"));
        });

        System.out.println("\nLog: ");
        Reflectors.printLog();
    }
}
