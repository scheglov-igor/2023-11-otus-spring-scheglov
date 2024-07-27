package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuestionDtoDaoImpl implements CsvQuestionDtoDao {

    private final ResourcesReader resourcesReader;

    @Override
    public List<QuestionDto> getQuestionDtoList() {
        try {
            Reader csvReader = resourcesReader.getResourceFileAsReader();

            List<QuestionDto> beans = new CsvToBeanBuilder(csvReader)
                    .withType(QuestionDto.class).withSeparator(';').withSkipLines(1).build().parse();
            return beans;
        } catch (IOException e) {
            throw new QuestionReadException("can't find questions file", e);
        }
    }
}
