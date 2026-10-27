using ec.edu.monster.Modelos;
using System;
using System.Collections.Generic;
using System.Text;

namespace ec.edu.monster.Interfaces;

public interface IAdaptadorAutenticacion
{
    TipoBackend Tipo { get; }
    Task<string> IniciarSesionAsync(string usuario, string contrasenia);
    Task<string> CambiarContraseniaAsync(string contraseniaActual, string contraseniaNueva, string token);
}