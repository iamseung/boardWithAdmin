package hello.core.function;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FunctionTest {
    @Test
    @DisplayName("of 테스트")
    void WhatIsOf() {
        // Given
        List<Object> objects = List.of();

        // When

        // Then
        for (Object object : objects) {
            System.out.println("object = " + object);
        }

        System.out.println(objects.size());
        System.out.println(objects.getClass());
    }
}
