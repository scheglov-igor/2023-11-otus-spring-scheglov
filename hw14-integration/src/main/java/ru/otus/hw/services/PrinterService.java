package ru.otus.hw.services;

import ru.otus.hw.dto.PaperBookDto;
import ru.otus.hw.models.Book;

import java.util.Optional;

public interface PrinterService {

	Optional<PaperBookDto> print(Optional<Book> book);
}
