/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.vista;

import ec.edu.monster.controlador.ConversionControlador;
import ec.edu.monster.modelo.ConversionModelo;
import ec.edu.monster.modelo.MensajesModelo;
import ec.edu.monster.modelo.TipoConversion;
import ec.edu.monster.util.EntradaUtil;

/**
 *
 * @author sebas
 */
public class ConversionVista {
    private static final String[] UNIDADES_LONGITUD = {"MILIMETRO", "CENTIMETRO", "METRO", "KILOMETRO", "YARDA"};
    private static final String[] UNIDADES_MASA = {"MILIGRAMO", "GRAMO", "KILOGRAMO", "TONELADA", "ONZA"};
    private static final String[] UNIDADES_TEMPERATURA = {"CELSIUS", "FAHRENHEIT", "KELVIN", "RANKINE"};

    private final MensajesModelo mensajes = new MensajesModelo();
    private final ConversionControlador conversionControlador;

    public ConversionVista(ConversionControlador conversionControlador) {
        this.conversionControlador = conversionControlador;
    }
    
    public void ejecutarMenu() {
        int opcion;
        do {
            mensajes.mostrarTitulo("MENU PRINCIPAL DE CONVERSIONES");
            mensajes.mostrarMensaje("1. Convertir Longitud");
            mensajes.mostrarMensaje("2. Convertir Masa");
            mensajes.mostrarMensaje("3. Convertir Temperatura");
            mensajes.mostrarMensaje("0. Salir");
            opcion = EntradaUtil.leerEntero("Seleccione una opcion:");
     
            switch(opcion){
                case 1 -> procesarConversion(TipoConversion.LONGITUD,UNIDADES_LONGITUD);
                case 2 -> procesarConversion(TipoConversion.MASA,UNIDADES_MASA);
                case 3 -> procesarConversion(TipoConversion.TEMPERATURA,UNIDADES_TEMPERATURA);
                case 0 -> mensajes.mostrarMensaje("Cerrando menu de conversion.");
                default -> mensajes.mostrarError("Opcion no valida.");
            }        
        }while(opcion != 0);
    }
    
    private void procesarConversion(TipoConversion tipo, String[] unidadesPermitidas) {
        mensajes.mostrarTitulo("CONVERSION DE" + tipo.getNombreVisible().toUpperCase());
        double valor = EntradaUtil.leerDecimal("Ingrese el valor a convertir:");
        String unidadInicial = solicitarUnidad("inicial", unidadesPermitidas) ;
        String unidadFinal = solicitarUnidad("final", unidadesPermitidas) ;
        
        ConversionModelo modelo = new ConversionModelo(tipo, valor, unidadInicial, unidadFinal);
        try {
            double resultado = conversionControlador.convertir(modelo);
            mensajes.mostrarMensaje(String.format("RESULTADO DEL SERVIDOR: %.4f %s = %.4f %s", valor, unidadInicial, resultado, unidadFinal));
        } catch (Exception ex) {
            mensajes.mostrarError("Error en la llamada SOAP: " + ex.getMessage());
        }

    }
    
    private String solicitarUnidad(String tipoUnidad, String[] unidadesPermitidas){
        mensajes.mostrarMensaje("Seleccione la unidad " + tipoUnidad + ":");
        for(int i = 0; i < unidadesPermitidas.length; i++){
            mensajes.mostrarMensaje((i + 1) + ". " + unidadesPermitidas[i]);
        } 
        while(true){
            int op = EntradaUtil.leerEntero("Opcion:");
            if( op >=1 && op <= unidadesPermitidas.length){
                return unidadesPermitidas[op-1];
            }
            mensajes.mostrarError("Unidad no válida.");
        }
    }
}
