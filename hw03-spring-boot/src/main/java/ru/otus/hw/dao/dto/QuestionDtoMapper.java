package ru.otus.hw.dao.dto;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionDtoMapper {
    public Question toDomainObject(QuestionDto questionDto) {
        return new Question(questionDto.getText(), questionDto.getAnswers());
    }

    public List<Question> toDomainObjectList(List<QuestionDto> questionDtoList) {
        return questionDtoList.stream().map(this::toDomainObject).collect(Collectors.toList());
    }
}
