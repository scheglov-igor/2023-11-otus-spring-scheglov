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

public class CsvQuestionDaoTest {

    @Test
    void testSuccesParsing(){

        TestFileNameProvider fileNameProvider = () -> "testCsvFile.csv";

        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));
        Question expectedQuestion = new Question("MyQuestion", answerList);
        List<Question> expectedQuestionList = new ArrayList<>();
        expectedQuestionList.add(expectedQuestion);

        QuestionDao dao = new CsvQuestionDao(fileNameProvider);
        Assertions.assertEquals(expectedQuestionList, dao.findAll());
    }

    @Test
    void exceptionTesting(){

        TestFileNameProvider fileNameProvider = () -> "noFile.csv";

        QuestionDao dao = new CsvQuestionDao(fileNameProvider);


        Assertions.assertThrows(QuestionReadException.class, dao::findAll);
    }

}
