package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.CsvQuestionDtoDao;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.dao.dto.QuestionDtoMapper;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public final class CsvQuestionDaoUnitTest {

    @Mock
    private CsvQuestionDtoDao csvQuestionDtoDao;
    @Mock
    private QuestionDtoMapper questionDtoMapper;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Test
    void testFindAll() {

        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));

        List<QuestionDto> questionDtoList = new ArrayList<>();
        var questionDto = new QuestionDto();
        questionDto.setText("MyQuestion");
        questionDto.setAnswers(answerList);
        questionDtoList.add(questionDto);

        given(csvQuestionDtoDao.getQuestionDtoList()).willReturn(questionDtoList);

        List<Question> questionList = new ArrayList<>();
        Question question = new Question("MyQuestion", answerList);
        questionList.add(question);

        given(questionDtoMapper.toDomainObjectList(questionDtoList)).willReturn(questionList);

        List<Question> expectedQuestionList = new ArrayList<>();
        Question expectedQuestion = new Question("MyQuestion", answerList);
        expectedQuestionList.add(expectedQuestion);

        assertEquals(expectedQuestionList, csvQuestionDao.findAll());
    }

}