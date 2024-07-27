package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.CsvQuestionDtoDao;
import ru.otus.hw.dao.CsvQuestionDtoDaoImpl;
import ru.otus.hw.dao.ResourcesReader;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = CsvQuestionDtoDaoImpl.class)
public class CsvQuestionDtoDaoImplBootTest {

    @MockBean
    private ResourcesReader resourcesReader;

    @Autowired
    CsvQuestionDtoDao csvQuestionDtoDao;

    @Test
    void testGetQuestionDtoList() throws IOException {

        List<QuestionDto> expectedQuestionDtoList = new ArrayList<>();
        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer("TrueAnswer", true));
        answerList.add(new Answer("FalseAnswer1", false));
        answerList.add(new Answer("FalseAnswer2", false));

        var questionDto = new QuestionDto();
        questionDto.setText("MyQuestion");
        questionDto.setAnswers(answerList);
        expectedQuestionDtoList.add(questionDto);


        String csvAsString = "# row to skip\nMyQuestion;TrueAnswer%true|FalseAnswer1%false|FalseAnswer2%false";
        given(resourcesReader.getResourceFileAsReader()).willReturn(new StringReader(csvAsString));

        assertEquals(expectedQuestionDtoList, csvQuestionDtoDao.getQuestionDtoList());

    }

    @Test
    void exceptionTesting() throws IOException {
        given(resourcesReader.getResourceFileAsReader()).willThrow(new IOException());
        assertThrows(QuestionReadException.class, csvQuestionDtoDao::getQuestionDtoList);
    }

}
