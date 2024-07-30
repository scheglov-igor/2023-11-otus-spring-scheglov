package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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

    private final QuestionDtoMapper questionDtoMapper;

    private final ResourcesReader resourcesReader;

    @Override
    public List<Question> findAll() {
        List<QuestionDto> questionDtoList = findAllDto();
        List<Question> questions = questionDtoMapper.toDomainObjectList(questionDtoList);
        return  questions;
    }

    private List<QuestionDto> findAllDto() {
        try (Reader csvReader = resourcesReader.getResourceFileAsReader()) {
            return new CsvToBeanBuilder(csvReader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new QuestionReadException("can't find questions file", e);
        }
    }
}