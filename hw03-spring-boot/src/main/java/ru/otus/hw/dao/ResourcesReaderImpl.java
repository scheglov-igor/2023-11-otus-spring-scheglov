package ru.otus.hw.dao;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Component
public class ResourcesReaderImpl implements ResourcesReader {

    public Reader getResourceFileAsReader(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);
        InputStream is = resource.getInputStream();
        Reader reader = new InputStreamReader(is);
        return reader;
    }
}
