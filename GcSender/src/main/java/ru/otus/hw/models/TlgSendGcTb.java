package ru.otus.hw.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tlg_send_gc_tb")
public class TlgSendGcTb {


    @Id
    @Column(name = "id")
    private Long id;

    //TODO мне тут rowid не нужен, достаочно в репозитории по нему вытаскивать.
//    @Column(name = "rowid")
    @Transient
    private String rowid;

    @Column(name = "tlg_date")
    private LocalDateTime tlgDate;

    @Column(name = "tlg_text")
    private String tlgText;

    @Column(name = "tlg_status")
    private Integer tlgStatus;

    @Column(name = "tlg_address")
    private String tlgAddress;

    @Column(name = "tlg_from")
    private String tlgFrom;

    @Column(name = "tlg_importance")
    private Integer tlgImportance;

    @Column(name = "tlg_webservice")
    private String tlgWebservice;

    @Column(name = "obj_id")
    private Long objId;

    @Column(name = "obj_type")
    private String objType;

    @Column(name = "obj_prop")
    private String objProp;

    @Column(name = "tlg_result")
    private String tlgResult;

    @Column(name = "tlg_timesending")
    private LocalDateTime tlgTimesending;

    @Column(name = "gc_sas_id")
    private Long gcSasId;

    @Column(name = "gc_sas_errorcode")
    private Integer gcSasErrorcode;

    @Column(name = "gc_sas_error_message")
    private String gcSasErrorMessage;

    @Column(name = "date_change")
    private LocalDateTime dateChange;

    @Column(name = "row_state")
    private Integer rowState;

    }
