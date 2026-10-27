package ec.edu.monster;

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