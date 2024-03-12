package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@jakarta.persistence.Entity
@Table(name = "history_contract")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryContract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Column
	@JsonIgnore
	private Long accountId;

	@Column
	@JsonIgnore
	private Long cardId;

	@Column
	@JsonIgnore
	private Long contractId;

	@Column
	@JsonIgnore
	private Date deleteDate;

	@Column
	@JsonIgnore
	private Long loadId;

	@Column
	@JsonIgnore
	private Long productId;

	@Column
	@JsonIgnore
	private Long tpvId;

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}

		HistoryContract that = (HistoryContract) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
			.getPersistentClass()
			.hashCode() : getClass().hashCode();
	}
}
