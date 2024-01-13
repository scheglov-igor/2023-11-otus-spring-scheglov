package ru.otus.hw.dao;

import java.io.IOException;
import java.io.Reader;

public interface ResourcesReader {
    Reader getResourceFileAsReader(String fileName) throws IOException;
}
