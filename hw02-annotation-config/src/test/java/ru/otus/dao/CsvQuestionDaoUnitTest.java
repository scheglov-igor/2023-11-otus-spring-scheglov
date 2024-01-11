package ru.otus.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.TestServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public final class CsvQuestionDaoUnitTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        csvQuestionDao = new CsvQuestionDao(fileNameProvider);
    }

    //TODO
    // А теперь это является юнит-тестом? Тут я замокал все зависимости.
    // если нет, я тогда не понимаю, что такое юнит-тесты в приложении со спрингом, где в каждом сервисе
    // целая куча зависимостей. Останется только геттеры-сеттеры тестировать =/
    @Test
    void testGetQuestionsFile() {
        given(fileNameProvider.getTestFileName()).willReturn(("testCsvFile.csv"));

        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));
        Question expectedQuestion = new Question("MyQuestion", answerList);
        List<Question> expectedQuestionList = new ArrayList<>();
        expectedQuestionList.add(expectedQuestion);

        assertThat(csvQuestionDao.findAll().equals(answerList));
    }

    //TODO
    // И еще вопрос. Вспомогательные методы, в этом классе я сделал публичными, только чтобы их протестировать
    // по идее они должны быть приватными.
    // как с ними правильно поступать? Не тестировать? (типа класс - это черный ящик)
    // Тестировать через рефлексию? Делать публичными?
    @Test
    void testConvertQuestionDtoList() {
        List<QuestionDto> questionDtoList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));
        var questionDto = new QuestionDto();
        questionDto.setText("MyQuestion");
        questionDto.setAnswers(answerList);
        questionDtoList.add(questionDto);

        Question expectedQuestion = new Question("MyQuestion", answerList);
        List<Question> expectedQuestionList = new ArrayList<>();
        expectedQuestionList.add(expectedQuestion);

        assertThat(csvQuestionDao.convertQuestionDtoList(questionDtoList).equals(expectedQuestionList));
    }

    @Test
    void exceptionTesting(){
        TestFileNameProvider fileNameProvider = () -> "noFile.csv";
        QuestionDao dao = new CsvQuestionDao(fileNameProvider);
        assertThrows(QuestionReadException.class, dao::findAll);
    }
}

