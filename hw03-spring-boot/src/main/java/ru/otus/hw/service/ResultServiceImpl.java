package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.dao.dto.StudentDto;
import ru.otus.hw.dao.dto.StudentDtoMapper;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final LocalizedIOService ioService;

    private final StudentDtoMapper studentMapper;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLineLocalized("ResultService.test.results");

        //TODO а вот это - правильная рабта с DTO?
        // ДТО - отдельно, маппер - отдельно, домен и дто друг про друга не знают
        // не совсем уверен, что создаю дто в правильном месте. Но больше не вижу подходящих мест.
        StudentDto studentDto = studentMapper.toDto(testResult.getStudent());

        ioService.printFormattedLineLocalized("ResultService.student",
                studentDto.getFullName());
        ioService.printFormattedLineLocalized("ResultService.answered.questions.count",
                testResult.getAnsweredQuestions().size());
        ioService.printFormattedLineLocalized("ResultService.right.answers.count",
                testResult.getRightAnswersCount());

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLineLocalized("ResultService.passed.test");
            return;
        }
        ioService.printLineLocalized("ResultService.fail.test");
    }
}
