package ru.otus.hw.services;

import ru.otus.hw.models.TlgSendGcTb;

import java.util.List;
import java.util.Optional;

public interface TlgSendGcTbService {

    List<TlgSendGcTb> selectAllByStatus();

    Optional<TlgSendGcTb> findByRowId(String rowid);

    Optional<TlgSendGcTb> findById(Long id);

    TlgSendGcTb save(TlgSendGcTb tlgSendGcTb);
}
