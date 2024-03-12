package com.banco.scheduler;

import com.banco.services.HistoryContractService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractScheduler {

	private final HistoryContractService historyContractService;

	private final SessionFactory sessionFactory;

	@Scheduled(fixedRate = 20000)
	@Transactional
	public void deleteDeactivateContract() {

		Session session = null;

		try {
			session = sessionFactory.openSession();
			session.beginTransaction();

			historyContractService.saveContractHistoric();

			session.getTransaction().commit();
		} catch (Exception ex) {
			if (session != null && session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}

		}

	}


}
