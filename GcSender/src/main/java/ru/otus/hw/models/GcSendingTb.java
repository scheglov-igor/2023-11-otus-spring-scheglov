package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gc_sending_tb")
public class GcSendingTb {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "IS_ACTIVE")
	private Integer isActive;

	@Column(name = "server_name")
	private String serverName;

	@Column(name = "ROW_STATE")
	private Integer rowState;

	@Column(name = "DATE_CHANGE")
	private LocalDateTime dateChange;

}
