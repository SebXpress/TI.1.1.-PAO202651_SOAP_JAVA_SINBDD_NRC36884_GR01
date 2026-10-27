/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

import ec.edu.monster.modelo.MensajesModelo;

public class PruebaMensajes {

    public static void main(String[] args) {

        MensajesModelo mensajes = new MensajesModelo();

        mensajes.mostrarEncabezado();

        mensajes.mostrarTitulo("MENU PRINCIPAL");
        mensajes.mostrarMensaje("1. Conversion de longitud");
        mensajes.mostrarMensaje("2. Conversion de masa");
        mensajes.mostrarMensaje("3. Conversion de temperatura");
        mensajes.mostrarMensaje("4. Salir");
    }
}