using ec.edu.monster.Modelos;

namespace ec.edu.monster.Servicios;

public class EstadoAplicacion
{
    public string Cliente { get; set; } = "WEB";
    public string UrlJavaSoap { get; private set; } = ConstantesRed.UrlJavaSoap;

    public void ConfigurarServidor(string host, int puerto, string contexto)
    {
        host = host.Trim();
        contexto = contexto.Trim().Trim('/');
        if (Uri.CheckHostName(host) == UriHostNameType.Unknown || host.Contains(':')
            || puerto < 1 || puerto > 65535
            || string.IsNullOrWhiteSpace(contexto)
            || contexto.Any(c => !char.IsAsciiLetterOrDigit(c) && c != '_' && c != '-' && c != '.'))
            throw new ArgumentException("Ingresa una IP IPv4 o nombre de equipo, puerto de 1 a 65535 y contexto válido.");
        string url = new UriBuilder("http", host, puerto, contexto + "/WSConversorUnidades").Uri.AbsoluteUri;
        if (UrlJavaSoap != url)
        {
            UrlJavaSoap = url;
            TokenActual = string.Empty;
            NotificarEstadoCambiado();
        }
    }

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