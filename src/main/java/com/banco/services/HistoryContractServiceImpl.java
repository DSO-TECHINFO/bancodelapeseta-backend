package com.banco.services;


import com.banco.entities.Contract;
import com.banco.entities.HistoryContract;
import com.banco.repositories.ContractRepository;
import com.banco.repositories.HistoryContractRepository;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class HistoryContractServiceImpl implements HistoryContractService {


	private final ContractRepository contractRepository;

	private final HistoryContractRepository historyContractRepository;

	@Override
	public void saveContractHistoric() {
		List<Contract> deactivatedContract = contractRepository.findContractDeactivated()
			.orElseThrow();

		log.info(String.format("Se va a proceder a guardar: '%s' contratos ", deactivatedContract.size()));
		deactivatedContract.stream()
			.map(this::historyContractMap)
			.forEach(historyContractRepository::save);
		log.info("Se han borrado correctamente y se han guardado en el historico");

		deactivatedContract.forEach(contractRepository::delete);

	}

	private HistoryContract historyContractMap(Contract contract) {
		return HistoryContract.builder()
				.accountId(contract.getAccount() != null ? contract.getAccount().getId() : null)
				.cardId(contract.getCard() != null ? contract.getCard().getId() : null)
				.tpvId(contract.getTpv() != null ? contract.getTpv().getId() : null)
				.productId(contract.getProduct() != null ? contract.getProduct().getId() : null)
				.deleteDate(new Date())
				.build();
	}

}
