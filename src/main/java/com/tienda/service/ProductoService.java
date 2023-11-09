package com.tienda.service;

import com.tienda.domain.Producto;
import java.util.List;

public interface ProductoService {

    // Se genera un método para obtener un ArrayList de Categorias
    public List<Producto> getProductos(boolean activos);

    // Se obtiene un Categoria, a partir del id de un categoria
    public Producto getProducto(Producto producto);

    // Se inserta un nuevo categoria si el id del categoria esta vacío
    // Se actualiza un categoria si el id del categoria NO esta vacío
    public void save(Producto producto);

    // Se elimina el categoria que tiene el id pasado por parámetro
    public void delete(Producto producto);

    // Ejemplo de una consulta con un Query
    public List<Producto> consultaQuery(double precioInf, double precioSup);
    
    // Ejemplo de una consulta con un JPQL
    public List<Producto> consultaJPQL(double precioInf, double precioSup);

}
