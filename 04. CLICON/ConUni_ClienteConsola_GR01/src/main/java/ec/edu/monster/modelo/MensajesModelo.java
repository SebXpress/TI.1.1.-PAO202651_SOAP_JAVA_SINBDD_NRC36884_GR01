/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author sebas
 */
public class MensajesModelo {
    public void mostrarEncabezado() {
        System.out.println(" __  __   ___   _   _   _____   _____   _____   _____");
        System.out.println("|  \\/  | / _ \\ | \\ | | / ____| |_   _| |  ___| |  __ \\");
        System.out.println("| \\  / || | | ||  \\| || (___     | |   | |__   | |__) |");
        System.out.println("| |\\/| || | | || . ` | \\___ \\    | |   |  __|  |  _  /");
        System.out.println("| |  | || |_| || |\\  | ____) |   | |   | |___  | | \\ \\");
        System.out.println("|_|  |_| \\___/ |_| \\_||_____/    |_|   |_____| |_|  \\_\\");
        System.out.println();
        System.out.println("\t\t  _____   _   _");
        System.out.println("\t\t / ____| | | | |");
        System.out.println("\t\t| |      | | | |");
        System.out.println("\t\t| |      | | | |");
        System.out.println("\t\t| |____  | |_| |");
        System.out.println("\t\t \\_____|  \\___/");
    }
    
    public void mostrarTitulo(String titulo) {
        System.out.println("\n==================================================");
        System.out.println(titulo);
        System.out.println("==================================================");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("ERROR: " + error);
    }
    
}
