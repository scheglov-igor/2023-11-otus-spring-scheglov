package ru.otus.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.ResourcesReader;
import ru.otus.hw.dao.ResourcesReaderImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourcesReaderImplTest {

    @Test
    void testGetResourceFileAsReader() throws IOException {

        String csvAsString = "# row to skipMyQuestion;TrueAnswer%true|FalseAnswer1%false|FalseAnswer2%false";

        ResourcesReader resourcesReader = new ResourcesReaderImpl();
        Reader reader = resourcesReader.getResourceFileAsReader("testCsvFile.csv");
        String content = "";
        try (var bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content = content + line;
            }
        }

        assertEquals(csvAsString, content);
    }
}
