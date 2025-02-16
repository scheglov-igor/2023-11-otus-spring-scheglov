package ru.otus.hw.services;

import ru.otus.hw.exception.CriticalStopException;
import ru.otus.hw.exception.ManualStopException;
import ru.otus.hw.models.GcSendXmlTb;
import ru.otus.hw.models.GcSendingTb;

import java.util.Optional;

public interface GcSendingTbService {

    Optional<GcSendingTb> selectGcSendingTb();

    void checkOtherGcSenders() throws ManualStopException, CriticalStopException;
    void startGcSender() throws CriticalStopException;
    void stopGcSender() throws CriticalStopException;

    public GcSendingTb save(GcSendingTb gcSendingTb);
}
