package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.dao.dto.QuestionDtoMapper;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    private final ResourcesReader resourcesReader;

    private final CsvQuestionDtoDao csvQuestionDtoDao;

    private final QuestionDtoMapper questionDtoMapper;

    @Override
    public List<Question> findAll() {

        try {
            String fileName = fileNameProvider.getTestFileName();
            Reader csvReader = resourcesReader.getResourceFileAsReader(fileName);
            List<QuestionDto> questionDtoList = csvQuestionDtoDao.getQuestionDtoList(csvReader);
            List<Question> questions = questionDtoMapper.toDomainObjectList(questionDtoList);
            return  questions;
        } catch (IOException e) {
            throw new QuestionReadException("can't find questions file", e);
        }
    }

}
