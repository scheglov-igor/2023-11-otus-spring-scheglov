package ru.otus.dao;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.ResourcesReaderImpl;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ResourcesReaderImplTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @Test
    void testGetResourceFileAsReader() throws IOException {

        var csvAsString = "# row to skip\r\nMyQuestion;TrueAnswer%true|FalseAnswer1%false|FalseAnswer2%false";

        given(fileNameProvider.getTestFileName()).willReturn("testCsvFile.csv");

        var resourcesReader = new ResourcesReaderImpl(fileNameProvider);
        var reader = resourcesReader.getResourceFileAsReader();
        var bufferedReader = new BufferedReader(reader);
        var content = IOUtils.toString(bufferedReader);

        assertEquals(csvAsString, content);
    }
}
