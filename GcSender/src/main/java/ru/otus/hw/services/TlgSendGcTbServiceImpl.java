package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.TlgSendGcTb;
import ru.otus.hw.repositories.TlgSendGcTbRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TlgSendGcTbServiceImpl implements TlgSendGcTbService{

    private final TlgSendGcTbRepository tlgSendGcTbRepository;

    @Override
    public List<TlgSendGcTb> selectAllByStatus() {
        LocalDateTime ldt = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        return tlgSendGcTbRepository.selectAllByStatus(0L, 0, 0, 4,"Telegrams.asmx", ldt);
    }

    @Override
    public Optional<TlgSendGcTb> findByRowId(String rowid) {
        Optional<TlgSendGcTb> optionalTlgSendGcTb = tlgSendGcTbRepository.findByRowId(rowid);
        optionalTlgSendGcTb.ifPresent(tlgSendGcTb1 -> tlgSendGcTb1.setRowid(rowid));
        return optionalTlgSendGcTb;
    };

    @Override
    public Optional<TlgSendGcTb> findById(Long id){
        return tlgSendGcTbRepository.findById(id);
    }

    @Override
    @Transactional
    public TlgSendGcTb save(TlgSendGcTb tlgSendGcTb) {
        tlgSendGcTb.setTlgTimesending(LocalDateTime.now());
        return tlgSendGcTbRepository.save(tlgSendGcTb);
    }
}
