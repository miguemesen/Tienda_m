
package com.tienda.domain;


import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name="producto")
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_producto")
    private Long idProducto;
//    private Long idCategoria;
    private String descripcion;            
    private String detalle;            
    private double precio;            
    private int existencias;            
    private String rutaImagen;
    private Boolean activo;
    
    @ToString.Exclude
    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="id_categoria")
    Categoria categoria;

    public Categoria getCategoria() {
        return this.categoria;
    }
    public boolean isActivo() {
        return activo;
    }
    
        public Producto() {
    }

}
