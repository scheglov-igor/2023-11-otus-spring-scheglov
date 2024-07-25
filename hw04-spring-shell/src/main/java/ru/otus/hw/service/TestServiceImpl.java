package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = askQuestion(question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestion(Question question) {
        var isAnswerValid = false;

        ioService.printLine(question.text());
        for (int i = 1; i <= question.answers().size(); i++) {
            ioService.printFormattedLine("%d. %s", i, question.answers().get(i - 1).text());
        }

        int answerNumber = ioService.readIntForRangeWithPromptLocalized(1, question.answers().size(),
                "enterAnswer", "cantReadAnswer");

        if (question.answers().get(answerNumber - 1).isCorrect()) {
            isAnswerValid = true;
        }

        return  isAnswerValid;
    }
}
