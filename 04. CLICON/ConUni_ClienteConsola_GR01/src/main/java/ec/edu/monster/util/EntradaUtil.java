/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.util;

import java.util.Scanner;

/**
 *
 * @author sebas
 */
public final class EntradaUtil {
    private static final Scanner ENTRADA = new Scanner(System.in);
    
    private EntradaUtil() {}

    public static String leerTextoNoVacio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = ENTRADA.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.err.println("Entrada invalida. No puede quedar vacio.");
        }
    }

    public static String leerTextoConDefecto(String mensaje, String defecto) {
        System.out.print(mensaje + " [" + defecto + "]: ");
        String texto = ENTRADA.nextLine().trim();
        return texto.isEmpty() ? defecto : texto;
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(ENTRADA.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.err.println("Entrada invalida. Debe ingresar un numero entero. Ej:1");
            }
        }
    }

    public static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(ENTRADA.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException ex) {
                System.err.println("Entrada invalida. Debe ingresar un numero decimal. Ej: 0.1");
            }
        }
    }
}