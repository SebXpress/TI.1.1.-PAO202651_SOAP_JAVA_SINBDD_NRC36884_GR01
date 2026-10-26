using System;
using System.Collections.Generic;
using System.Text;

namespace ec.edu.monster.Modelos;

public class PeticionConversion
{
    public double Valor { get; set; }
    public string UnidadOrigen {  get; set; } = string.Empty;
    public string UnidadDestino {  get; set; } = string.Empty;
    public string TipoConversion {  get; set; } = "Longitud";
}
