/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author sebas
 */
public enum TipoConversion {
    
    LONGITUD("Longitud"),
    MASA("Masa"),
    TEMPERATURA("Temperatura"),;
    
    private String nombreVisible;

    TipoConversion(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getNombreVisible(){
        return nombreVisible;
    }

}
