package com.banco.services;

import com.banco.repositories.LoanRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    LoanRepository loanRepository;
}
