package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final ResultService resultService;

    @Override
    public TestResult executeTestFor(Student student) {
        return testService.executeTestFor(student);
    }

    @Override
    public void showResult(TestResult testResult) {
        resultService.showResult(testResult);
    }
}
