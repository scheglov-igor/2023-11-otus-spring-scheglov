package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.PaperBookDto;
import ru.otus.hw.models.Book;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrinterServiceImpl implements PrinterService {

	private final BookConverter bookConverter;

	public Optional<PaperBookDto> print(Optional<Book> book) {

		log.info("Printing ...");
		delay();
		log.info("Printing done");

		return book.map(bookConverter::toPaperBookDto);

	}

	private static void delay() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
