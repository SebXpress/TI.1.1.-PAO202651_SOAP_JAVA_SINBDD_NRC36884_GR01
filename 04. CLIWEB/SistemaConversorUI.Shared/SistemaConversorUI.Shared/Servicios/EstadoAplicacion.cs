using ec.edu.monster.Modelos;

namespace ec.edu.monster.Servicios;

public class EstadoAplicacion
{
    public TipoBackend BackendActual { get; set; } = TipoBackend.JavaSoap;
    public string TokenActual { get; set; } = string.Empty;
    public event Action? AlCambiar;

    public void EstablecerBackend(TipoBackend tipo)
    {
        if (BackendActual != tipo)
        {
            BackendActual = tipo;
            TokenActual = string.Empty;
            NotificarEstadoCambiado();
        }
    }

    private void NotificarEstadoCambiado() => AlCambiar?.Invoke();
}