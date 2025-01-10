package ru.otus.hw.services;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.dto.PaperBookDto;

import java.util.Optional;

@MessagingGateway
public interface BookPrinterGateway {

	@Gateway(requestChannel = "bookChannel", replyChannel = "paperBookChannel")
	Optional<PaperBookDto> process(String bookId);
}
