using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Configuration;
using ec.edu.monster.Adaptadores;
using ec.edu.monster.Interfaces;
using ec.edu.monster.Servicios;

namespace SistemaConversorUI.Maui
{
    public static class MauiProgram
    {
        public static MauiApp CreateMauiApp()
        {
            var builder = MauiApp.CreateBuilder();
            builder
                .UseMauiApp<App>()
                .ConfigureFonts(fonts =>
                {
                    fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                });

            using (var config = typeof(MauiProgram).Assembly.GetManifestResourceStream("SistemaConversorUI.Maui.appsettings.json")
                ?? throw new InvalidOperationException("Falta appsettings.json"))
                builder.Configuration.AddJsonStream(config);
            builder.Services.AddMauiBlazorWebView();

            // HttpClient simple para Android
            builder.Services.AddSingleton<HttpClient>();

            // Servicios del estado y fábrica
            builder.Services.AddScoped<EstadoAplicacion>(_ => {
                var estado = new EstadoAplicacion { Cliente = DeviceInfo.Platform == DevicePlatform.WinUI ? "ESCRITORIO" : "MOVIL" };
                estado.ConfigurarServidor(builder.Configuration["ServidorSoap:Host"] ?? "localhost",
                    int.Parse(builder.Configuration["ServidorSoap:Puerto"] ?? "8080"),
                    builder.Configuration["ServidorSoap:Contexto"] ?? "WS_ConUni_Java_SOAP_GR01");
                return estado;
            });
            builder.Services.AddScoped<FabricaEstrategiaBackend>();

            // Adaptadores Java SOAP
            builder.Services.AddScoped<IAdaptadorAutenticacion, AdaptadorAutenticacionJavaSoap>();
            builder.Services.AddScoped<IAdaptadorConversion, AdaptadorConversionJavaSoap>();

#if DEBUG
            builder.Services.AddBlazorWebViewDeveloperTools();
            builder.Logging.AddDebug();
#endif

            return builder.Build();
        }
    }
}