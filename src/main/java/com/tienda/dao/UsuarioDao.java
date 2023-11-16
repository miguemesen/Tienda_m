/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tienda.dao;

import com.tienda.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author miguemesen
 */
public interface UsuarioDao extends JpaRepository<Usuario,Long> {
    
    // Se hace una búsqueda por username
    public Usuario findByUsername(String username);
}
