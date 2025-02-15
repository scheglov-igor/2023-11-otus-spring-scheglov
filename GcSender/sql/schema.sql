-- таблица, за которой нужно следить
CREATE TABLE tlg_send_gc_tb (
    id BIGSERIAL,
    tlg_date TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    tlg_text TEXT,
    tlg_status INTEGER,
    tlg_address VARCHAR(4000),
    tlg_from VARCHAR(69),
    tlg_importance INTEGER DEFAULT 0 NOT NULL,
    tlg_webservice VARCHAR(128) DEFAULT 'telegrams.asmx'::character varying NOT NULL,
    obj_id BIGINT,
    obj_type VARCHAR(32),
    obj_prop VARCHAR(512),
    tlg_result VARCHAR(512),
    tlg_timesending TIMESTAMP(6) WITHOUT TIME ZONE,
    gc_sas_id BIGINT,
    gc_sas_errorcode INTEGER,
    gc_sas_error_message VARCHAR(4000),
    date_change TIMESTAMP(0) WITHOUT TIME ZONE DEFAULT LOCALTIMESTAMP NOT NULL,
    row_state SMALLINT DEFAULT 0 NOT NULL,
    CONSTRAINT tlg_send_gc_tb_pkey PRIMARY KEY(id)
);


CREATE OR REPLACE FUNCTION notify_change_tlg_send_gc_tb (
)
    RETURNS trigger LANGUAGE 'plpgsql'
    VOLATILE
    CALLED ON NULL INPUT
    SECURITY INVOKER
    PARALLEL UNSAFE
    COST 100
AS
$body$
BEGIN
    if (NEW.tlg_status = 0) THEN
        PERFORM pg_notify('notify_channel_tlg_send_gc_tb', NEW.id::varchar);
    END IF;
    RETURN NEW;
END;
$body$;


CREATE TRIGGER tlg_send_gc_tb_ai_trg
    AFTER INSERT
    ON tlg_send_gc_tb

    FOR EACH ROW
EXECUTE PROCEDURE notify_change_tlg_send_gc_tb();


-- таблица для сохранения результата каждой попытки отправить сообщение
CREATE TABLE gc_send_xml_tb (
    id BIGSERIAL,
    gc_service VARCHAR(20) NOT NULL,
    data_id BIGINT  NOT NULL,
    request_xml TEXT,
    response_xml TEXT,
    status SMALLINT  DEFAULT 0,
    result VARCHAR(1000),
    gc_function VARCHAR(20),
    row_state SMALLINT  DEFAULT 0 NOT NULL,
    date_change TIMESTAMP(6) WITHOUT TIME ZONE  DEFAULT LOCALTIMESTAMP,
    CONSTRAINT gc_send_xml_tb_pkey PRIMARY KEY(id)
) ;


-- определяем активный сендер
CREATE TABLE gc_sending_tb (
   id BIGINT NOT NULL,
   date_change TIMESTAMP WITHOUT TIME ZONE  DEFAULT LOCALTIMESTAMP NOT NULL,
   is_active SMALLINT,
   row_state SMALLINT DEFAULT 0 NOT NULL,
   server_name VARCHAR,
   CONSTRAINT gc_sender_tb_pkey PRIMARY KEY(id)
) ;


