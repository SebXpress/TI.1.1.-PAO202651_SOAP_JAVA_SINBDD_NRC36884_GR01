using ec.edu.monster.Modelos;
using System;
using System.Collections.Generic;
using System.Text;

namespace ec.edu.monster.Interfaces;

public interface IAdaptadorConversion
{
    TipoBackend Tipo { get; }
    Task<double> ConvertirAsync(PeticionConversion peticion, string token);
}