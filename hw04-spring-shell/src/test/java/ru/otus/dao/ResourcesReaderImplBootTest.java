package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.ResourcesReader;
import ru.otus.hw.dao.ResourcesReaderImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = ResourcesReaderImpl.class)
public class ResourcesReaderImplBootTest {

    @MockBean
    private TestFileNameProvider fileNameProvider;

    @Autowired
    ResourcesReader resourcesReader;

    @Test
    void testGetResourceFileAsReader() throws IOException {

        String csvAsString = "# row to skipMyQuestion;TrueAnswer%true|FalseAnswer1%false|FalseAnswer2%false";

        given(fileNameProvider.getTestFileName()).willReturn("testCsvFile.csv");

        Reader reader = resourcesReader.getResourceFileAsReader();

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
