package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.GcSendXmlTb;
import ru.otus.hw.repositories.GcSendXmlTbRepository;

@Service
@RequiredArgsConstructor
public class GcSendXmlTbServiceImpl implements GcSendXmlTbService {

    private final GcSendXmlTbRepository gcSendXmlTbRepository;

    @Override
    @Transactional
    public GcSendXmlTb save(GcSendXmlTb gcSendXmlTb) {
        return gcSendXmlTbRepository.save(gcSendXmlTb);
    }
}
