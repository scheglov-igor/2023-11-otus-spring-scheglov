package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Component
@RequiredArgsConstructor
public class ResourcesReaderImpl implements ResourcesReader {


    private final TestFileNameProvider fileNameProvider;

    public Reader getResourceFileAsReader() throws IOException {
        Resource resource = new ClassPathResource(fileNameProvider.getTestFileName());
        InputStream is = resource.getInputStream();
        Reader reader = new InputStreamReader(is);
        return reader;
    }
}
