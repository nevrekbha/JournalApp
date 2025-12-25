package net.engineeringdigest.journalingApp.service;

import net.engineeringdigest.journalingApp.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(User.builder().name("Ram").password("Ram").build()),
                Arguments.of(User.builder().name("Snehal").password("").build())
        );

    }
}
