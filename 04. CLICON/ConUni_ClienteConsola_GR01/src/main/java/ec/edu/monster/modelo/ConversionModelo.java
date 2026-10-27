/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author sebas
 */
public class ConversionModelo {
    private TipoConversion tipoConversion;
    private double valor;
    private String valorInicial;
    private String valorFinal;

    public ConversionModelo(TipoConversion tipoConversion, double valor, String valorInicial, String valorFinal) {
        this.tipoConversion = tipoConversion;
        this.valor = valor;
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
    }

    public TipoConversion getTipoConversion() {
        return tipoConversion;
    }

    public double getValor() {
        return valor;
    }

    public String getValorInicial() {
        return valorInicial;
    }

    public String getValorFinal() {
        return valorFinal;
    }
    
    
}
