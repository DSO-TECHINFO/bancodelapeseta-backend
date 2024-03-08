package com.banco.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.repositories.TpvRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TpvServiceImpl implements TpvService {
    
    private TpvRepository tpvRepository;
}
