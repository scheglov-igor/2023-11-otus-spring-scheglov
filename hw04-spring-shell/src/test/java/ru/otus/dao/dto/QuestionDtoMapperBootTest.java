package ru.otus.dao.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.dao.dto.QuestionDtoMapper;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = QuestionDtoMapper.class)
public class QuestionDtoMapperBootTest {

    @Autowired
    QuestionDtoMapper questionDtoMapper;

    @Test
    void toDomainObjectTest() {
        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));
        var questionDto = new QuestionDto();
        questionDto.setText("MyQuestion");
        questionDto.setAnswers(answerList);

        Question question = questionDtoMapper.toDomainObject(questionDto);

        Question expectedQuestion = new Question("MyQuestion", answerList);

        assertEquals(expectedQuestion, question);
    }


    @Test
    void toDomainObjectListTest() {

        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));

        List<Question> expectedQuestionList = new ArrayList<>();
        Question expectedQuestion = new Question("MyQuestion", answerList);
        expectedQuestionList.add(expectedQuestion);

        List<QuestionDto> questionDtoList = new ArrayList<>();
        var questionDto = new QuestionDto();
        questionDto.setText("MyQuestion");
        questionDto.setAnswers(answerList);
        questionDtoList.add(questionDto);

        List<Question> questionList = questionDtoMapper.toDomainObjectList(questionDtoList);

        assertEquals(expectedQuestionList, questionList);
    }


}
