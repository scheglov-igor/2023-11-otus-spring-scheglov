package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.BookService;

@Component
@RequiredArgsConstructor
public class EmptyLibraryHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        boolean isLibraryEmpty = bookService.isLibraryEmpty();
        if (isLibraryEmpty) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Библиотека пуста!")
                    .build();
        } else {
            return Health.up()
                    .withDetail("message", "Библиотека полна!")
                    .build();
        }
    }
}
