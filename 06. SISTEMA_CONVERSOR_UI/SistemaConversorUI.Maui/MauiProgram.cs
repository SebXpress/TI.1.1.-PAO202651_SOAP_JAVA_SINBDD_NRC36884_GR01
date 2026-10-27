using Microsoft.Extensions.Logging;
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

            builder.Services.AddMauiBlazorWebView();

            // HttpClient simple para Android
            builder.Services.AddSingleton<HttpClient>();

            // Servicios del estado y fábrica
            builder.Services.AddScoped<EstadoAplicacion>();
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