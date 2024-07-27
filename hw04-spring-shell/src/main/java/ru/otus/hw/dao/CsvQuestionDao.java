package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.dao.dto.QuestionDtoMapper;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {

    private final CsvQuestionDtoDao csvQuestionDtoDao;

    private final QuestionDtoMapper questionDtoMapper;

    @Override
    public List<Question> findAll() {
        List<QuestionDto> questionDtoList = csvQuestionDtoDao.getQuestionDtoList();
        List<Question> questions = questionDtoMapper.toDomainObjectList(questionDtoList);
        return  questions;
    }

}
