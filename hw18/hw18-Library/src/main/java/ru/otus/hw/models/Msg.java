package ru.otus.hw.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@RequiredArgsConstructor
@Document(collection = "msg")
public class Msg {
    // The ID of the product
    @Id
    private final String id;

    private final long msgNo;

    private final String note;


}