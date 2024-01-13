package ru.otus.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.CsvQuestionDtoDao;
import ru.otus.hw.dao.CsvQuestionDtoDaoImpl;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Answer;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CsvQuestionDtoDaoImplTest {


    @Test
    void testGetQuestionDtoList() {

        List<QuestionDto> expectedQuestionDtoList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));
        var questionDto = new QuestionDto();
        questionDto.setText("MyQuestion");
        questionDto.setAnswers(answerList);
        expectedQuestionDtoList.add(questionDto);


        CsvQuestionDtoDao csvQuestionDtoDao = new CsvQuestionDtoDaoImpl();

        String csvAsString = "# row to skip\nMyQuestion;TrueAnswer%true|FalseAnswer1%false|FalseAnswer2%false";
        Reader reader = new StringReader(csvAsString);

        List<QuestionDto> questionDtoList = csvQuestionDtoDao.getQuestionDtoList(reader);

        assertEquals(questionDtoList, expectedQuestionDtoList);

    }

}
