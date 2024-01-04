package ru.otus.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvQuestionDaoTest {

    // тест из прошлого ДЗ - интеграционный
    @Test
    void testSuccessParsing(){

        TestFileNameProvider fileNameProvider = () -> "testCsvFile.csv";

        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));
        Question expectedQuestion = new Question("MyQuestion", answerList);
        List<Question> expectedQuestionList = new ArrayList<>();
        expectedQuestionList.add(expectedQuestion);

        QuestionDao dao = new CsvQuestionDao(fileNameProvider);
        assertEquals(expectedQuestionList, dao.findAll());
    }

}
