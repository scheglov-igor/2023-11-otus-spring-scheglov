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


    //TODO
    // У меня вопрос по поводу тестирования этого метода
    // На мой взгляд, он достаточно сложный, так что оставить его без тестов - как-то нелогично
    // В прошлом ДЗ я задавал вопрос Виталию про тестирование приватных методов,
    // получил ответ, что они тестируются в процессе связанных публичных - и это нормально
    //
    // Буду благодарен получить Ваше мнение на этот вопрос. Нужно ли его тестировать?
    //
    // В процессе написания вопроса появилась идея, что если вынести его в отдельный
    // CsvQuestionDtoReader, как Вы предлагали, то этот вопрос исчез бы.
    //
    // Получается, если мы хотим тестировать приватный метод - нужно вынести его в отдельный класс
    // и сделать публичным?

    private List<QuestionDto> findAllDto() {
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