package ru.otus.hw.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.BookData;

@FeignClient(name = "bookInfo", url = "http://${app.additional.host}:${app.additional.port}"/*, path = "/address-service"*/)
public interface BookAdditionalInfoClient {

    @GetMapping(value = "/additional-info", consumes = "application/json")
    ResponseEntity<BookData> getAdditionalInfo(@RequestParam("name") String nameVal);
}

