insert into TLG_SEND_GC_TB (tlg_text, tlg_status, tlg_webservice, tlg_date, tlg_importance,  row_state, date_change)
values ('<SendActiveRstrArea xmlns="http://SOME/WSERVICE"><id>-1</id><areaCode>UUP64</areaCode><lowLevel>GND</lowLevel><highLevel>1050AGL</highLevel><dateFrom>2025-02-23T00:00:00Z</dateFrom><dateTo>2025-02-24T23:59:00Z</dateTo><exceptionMsg></exceptionMsg></SendActiveRstrArea>',
        0, 'SendActiveRstrArea', current_timestamp, 1, 0, current_timestamp);


select * from tlg_send_gc_tb;

select * from gc_send_xml_tb;