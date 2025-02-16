package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.GcSendXmlTb;

@Repository
public interface GcSendXmlTbRepository extends JpaRepository<GcSendXmlTb, Long> {
}
