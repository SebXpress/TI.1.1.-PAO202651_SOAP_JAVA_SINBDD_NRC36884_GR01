using ec.edu.monster.Interfaces;
using ec.edu.monster.Modelos;


namespace ec.edu.monster.Servicios;

public class FabricaEstrategiaBackend
{
    private readonly IEnumerable<IAdaptadorConversion> _adaptadoresConversion;
    private readonly IEnumerable<IAdaptadorAutenticacion> _adaptadoresAutenticacion;

    public FabricaEstrategiaBackend(
        IEnumerable<IAdaptadorConversion> adaptadoresConversion,
        IEnumerable<IAdaptadorAutenticacion> adaptadoresAutenticacion)
    {
        _adaptadoresConversion = adaptadoresConversion;
        _adaptadoresAutenticacion = adaptadoresAutenticacion;
    }

    public IAdaptadorConversion ObtenerAdaptadorConversion(TipoBackend tipo) =>
        _adaptadoresConversion.FirstOrDefault(a => a.Tipo == tipo)
        ?? throw new NotSupportedException($"Adaptador de conversión para {tipo} no implementado.");

    public IAdaptadorAutenticacion ObtenerAdaptadorAutenticacion(TipoBackend tipo) =>
        _adaptadoresAutenticacion.FirstOrDefault(a => a.Tipo == tipo)
        ?? throw new NotSupportedException($"Adaptador de autenticación para {tipo} no implementado.");
}