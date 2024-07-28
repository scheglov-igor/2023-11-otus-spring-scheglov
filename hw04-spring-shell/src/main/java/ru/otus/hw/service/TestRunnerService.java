package ru.otus.hw.service;

import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

public interface TestRunnerService {

    TestResult executeTestFor(Student student);

    void showResult(TestResult testResult);
}
