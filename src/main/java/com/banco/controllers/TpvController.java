package com.banco.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.banco.services.TpvService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/tpv")
public class TpvController {

    private TpvService tpvService;
    
}
