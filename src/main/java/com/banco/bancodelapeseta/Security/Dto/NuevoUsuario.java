/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banco.bancodelapeseta.Security.Dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author salguero
 */
public class NuevoUsuario {
    
    @NotBlank
    private String nombre;
    @NotBlank
    private String nombreUsuario;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private Set<String> roles = new HashSet<>();

    //Gettes & Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
