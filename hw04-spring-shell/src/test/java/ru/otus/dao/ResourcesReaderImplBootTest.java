package ru.otus.dao;

import org.apache.commons.io.IOUtils;
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
    private ResourcesReader resourcesReader;

    @Test
    void testGetResourceFileAsReader() throws IOException {

        var csvAsString = "# row to skip\r\nMyQuestion;TrueAnswer%true|FalseAnswer1%false|FalseAnswer2%false";

        given(fileNameProvider.getTestFileName()).willReturn("testCsvFile.csv");

        var reader = resourcesReader.getResourceFileAsReader();
        var bufferedReader = new BufferedReader(reader);
        var content = IOUtils.toString(bufferedReader);

        assertEquals(csvAsString, content);

    }


}
