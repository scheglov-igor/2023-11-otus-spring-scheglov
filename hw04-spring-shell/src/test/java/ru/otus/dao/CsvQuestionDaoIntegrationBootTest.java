package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.ResourcesReader;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.dao.dto.QuestionDtoMapper;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {CsvQuestionDao.class, QuestionDtoMapper.class})
public class CsvQuestionDaoIntegrationBootTest {

    @MockBean
    private ResourcesReader resourcesReader;

    @Autowired
    private QuestionDao csvQuestionDao;

    @Test
    void testFindAll() throws IOException {

        String csvAsString = "# row to skip\nMyQuestion;TrueAnswer%true|FalseAnswer1%false|FalseAnswer2%false";
        given(resourcesReader.getResourceFileAsReader()).willReturn(new StringReader(csvAsString));

        List<Answer> expectedAnswerList = new ArrayList<>();
        expectedAnswerList.add(new Answer("TrueAnswer", true));
        expectedAnswerList.add(new Answer("FalseAnswer1", false));
        expectedAnswerList.add(new Answer("FalseAnswer2", false));

        List<Question> expectedQuestionList = new ArrayList<>();
        Question expectedQuestion = new Question("MyQuestion", expectedAnswerList);
        expectedQuestionList.add(expectedQuestion);

        assertEquals(expectedQuestionList, csvQuestionDao.findAll());
    }

}