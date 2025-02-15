package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.GcSendXmlTb;
import ru.otus.hw.models.GcSendingTb;

@Repository
public interface GcSendingTbRepository extends JpaRepository<GcSendingTb, Long> {
}
