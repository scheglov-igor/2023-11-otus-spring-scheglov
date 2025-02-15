package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.TlgSendGcTb;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TlgSendGcTbRepository extends JpaRepository<TlgSendGcTb, Long> {

/*
    "select id, tlg_date, tlg_text, tlg_status, row_state, "
    + "date_change, tlg_webservice, obj_id, obj_type, obj_prop, "
    + "tlg_result, tlg_timesending, tlg_address, tlg_from, tlg_importance, "
    + "GC_SAS_ID,  GC_SAS_ERRORCODE, GC_SAS_ERROR_MESSAGE from TLG_SEND_GC_TB "
    + "where id > ? and row_state = ? and (tlg_status = ? or tlg_status = ?) and tlg_webservice != 'Telegrams.asmx' "
    + "and (tlg_date >= current_timestamp - interval '1' day or date_change >= current_timestamp - interval '1' day) "
    + "order by TLG_DATE asc";
*/

    @Query("select t " +
           "from TlgSendGcTb t " +
           "where t.id > (:id) " +
           "and t.rowState = (:rowState) " +
           "and (tlgStatus = (:tlgStatus1) or tlgStatus = (:tlgStatus2)) " +
           "and tlgWebservice != (:tlgWebservice) " +
           "and (tlgDate >= (:yesterday) or dateChange >= (:yesterday)) " +
           "order by tlgDate asc")
    List<TlgSendGcTb> selectAllByStatus(@Param("id") Long id,
                                        @Param("rowState") Integer rowState,
                                        @Param("tlgStatus1") Integer tlgStatus1,
                                        @Param("tlgStatus2") Integer tlgStatus2,
                                        @Param("tlgWebservice") String tlgWebservice,
                                        @Param("yesterday") LocalDateTime yesterday);


/*
    final private String SQL_PG_TLGSEND_SELECT_BY_ID = "select id, tlg_date, tlg_text, tlg_status, row_state, "
                                                       + "date_change, tlg_webservice, obj_id, obj_type, obj_prop, "
                                                       + "tlg_result, tlg_timesending, tlg_address, tlg_from, tlg_importance, "
                                                       + "GC_SAS_ID,  GC_SAS_ERRORCODE, GC_SAS_ERROR_MESSAGE from TLG_SEND_GC_TB "
                                                       + "where id = ?";

    final private String SQL_ORA_TLGSEND_SELECT_BY_ROWID = "select rowid, id, tlg_date, tlg_text, tlg_status, row_state, "
                                                           + "date_change, tlg_webservice, obj_id, obj_type, obj_prop, "
                                                           + "tlg_result, tlg_timesending, tlg_address, tlg_from, tlg_importance, "
                                                           + "GC_SAS_ID,  GC_SAS_ERRORCODE, GC_SAS_ERROR_MESSAGE from TLG_SEND_GC_TB "
                                                           + "where rowid = ?";
*/


    @Query(value = "select t.* " +
           "from tlg_send_gc_tb t " +
           "where rowid = (:rowid)", nativeQuery = true)
    Optional<TlgSendGcTb> findByRowId(@Param("rowid") String rowid);

}