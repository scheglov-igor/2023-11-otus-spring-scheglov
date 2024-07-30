package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.ResourcesReader;
import ru.otus.hw.dao.ResourcesReaderImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ResourcesReaderImplTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @Test
    void testGetResourceFileAsReader() throws IOException {

        String csvAsString = "# row to skipMyQuestion;TrueAnswer%true|FalseAnswer1%false|FalseAnswer2%false";

        given(fileNameProvider.getTestFileName()).willReturn("testCsvFile.csv");

        ResourcesReader resourcesReader = new ResourcesReaderImpl(fileNameProvider);

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
