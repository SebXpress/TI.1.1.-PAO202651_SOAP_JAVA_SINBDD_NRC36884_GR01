/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelos.utilidades;

/**
 *
 * @author sebas
 */
public interface IConversor<T> {
    double convertir(double valor, T unidadInicial, T unidadFinal);
}
