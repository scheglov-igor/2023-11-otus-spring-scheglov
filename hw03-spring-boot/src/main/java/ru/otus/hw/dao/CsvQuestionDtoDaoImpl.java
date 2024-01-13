package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.dto.QuestionDto;

import java.io.Reader;
import java.util.List;

@Component
public class CsvQuestionDtoDaoImpl implements CsvQuestionDtoDao {

    @Override
    public List<QuestionDto> getQuestionDtoList(Reader reader) {
        List<QuestionDto> beans = new CsvToBeanBuilder(reader)
                .withType(QuestionDto.class).withSeparator(';').withSkipLines(1).build().parse();
        return beans;
    }
}
