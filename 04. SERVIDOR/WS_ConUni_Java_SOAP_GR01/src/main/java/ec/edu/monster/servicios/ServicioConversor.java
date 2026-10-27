/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.servicios;

import ec.edu.monster.modelos.utilidades.IConversor;

/**
 *
 * @author sebas
 */
public class ServicioConversor<T> {
    private final IConversor<T> conversor;
    
    // se inyecta la estrategia concreta del conversor a traves del constructor
    public ServicioConversor(IConversor<T> conversor){
        this.conversor = conversor;
    }
    
    public double conversor(double valor, T valorInicial, T valorFinal){
        if(conversor == null){
            throw new IllegalArgumentException("No se ha inyectado un conversor");
        }
        return conversor.convertir(valor, valorInicial, valorFinal);
    }
    
}
