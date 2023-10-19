
package com.tienda.domain;


import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name="categoria")
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_categoria")
    private Long idCategoria;
    private String descripcion;
    private String rutaImagen;
    private Boolean activo;

    public boolean isActivo() {
        return activo;
    }
    
        public Categoria() {
    }

    public Categoria(String description, boolean activo) {
        this.descripcion = descripcion;
        this.activo = activo;
    }
}
