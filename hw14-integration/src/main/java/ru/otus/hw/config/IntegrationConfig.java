package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.PrinterService;

@Configuration
public class IntegrationConfig {

	@Bean
	public MessageChannelSpec<?, ?> bookChannel() {
		return MessageChannels.direct();
	}

	@Bean
	public MessageChannelSpec<?, ?> paperBookChannel() {
		return MessageChannels.direct();
	}

	@Bean
	public IntegrationFlow printFlow(PrinterService printerService, BookRepository bookRepository) {
		return IntegrationFlow.from(bookChannel())
				.handle(bookRepository, "findById")
				.handle(printerService, "print")
				.channel(paperBookChannel())
				.get();
	}
}