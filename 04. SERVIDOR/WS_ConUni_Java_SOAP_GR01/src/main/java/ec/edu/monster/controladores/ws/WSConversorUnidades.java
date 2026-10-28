package ec.edu.monster.controladores.ws;

import ec.edu.monster.modelos.utilidades.ConversorLongitud;
import ec.edu.monster.modelos.utilidades.ConversorMasa;
import ec.edu.monster.modelos.utilidades.ConversorTemperatura;
import ec.edu.monster.modelos.utilidades.unidades.UnidadLongitud;
import ec.edu.monster.modelos.utilidades.unidades.UnidadMasa;
import ec.edu.monster.modelos.utilidades.unidades.UnidadTemperatura;
import ec.edu.monster.seguridad.AdministradorCredenciales;
import ec.edu.monster.seguridad.AdministradorTokens;
import ec.edu.monster.servicios.ServicioConversor;

import jakarta.jws.HandlerChain;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

/**
 *
 * @author sebas
 */
@WebService(serviceName = "WSConversorUnidades")
@HandlerChain(file = "manejador.xml")
public class WSConversorUnidades {

    /** Estado público mínimo para verificar conectividad sin iniciar sesión. */
    @WebMethod(operationName = "obtenerEstado")
    public ec.edu.monster.servicios.MonitorServidor.Estado obtenerEstado() {
        return ec.edu.monster.servicios.MonitorServidor.obtenerEstado();
    }

    @WebMethod(operationName = "cerrarSesion")
    public String cerrarSesion(@WebParam(name = "token") String token) {
        AdministradorTokens.InvalidarToken(token);
        return "Sesion cerrada";
    }

    private final ServicioConversor<UnidadLongitud> servicioLongitud = new ServicioConversor<>(new ConversorLongitud());
    private final ServicioConversor<UnidadMasa> servicioMasa = new ServicioConversor<>(new ConversorMasa());
    private final ServicioConversor<UnidadTemperatura> servicioTemperatura = new ServicioConversor<>(new ConversorTemperatura());
    
    
    /**
     * Permite autenticarse y obtener un token de sesión.
     */
    @WebMethod(operationName = "login")
    public String login(
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "contrasenia") String contrasenia){
    
        if(AdministradorCredenciales.validarCredenciales(usuario, contrasenia)){
            return AdministradorTokens.generarToken();
        }
        throw new RuntimeException("Credenciales incorrectas");
    }
    
    
    /**
     * Permite cambiar la contraseña global.
     */
    @WebMethod(operationName = "cambiarContrasenia")
    public String cambiarContrasenia(
            @WebParam(name = "contraseniaActual") String contraseniaActual,
            @WebParam(name = "contraseniaNueva") String contraseniaNueva){
            
        AdministradorCredenciales.cambiarContrasenia(contraseniaActual, contraseniaNueva);
        return "Contraseña cambiada con exito.";
    }
    
    /**
    * Conversion de unidades de longitud
    */
    
    @WebMethod(operationName = "convertirLongitud")
    public double ConvertirLongitud(
            @WebParam(name = "valor") double valor ,
            @WebParam(name = "unidadInicial") String unidadInicial ,
            @WebParam(name = "unidadFinal") String unidadFinal){
        
        UnidadLongitud origen = UnidadLongitud.valueOf(unidadInicial.toUpperCase());
        UnidadLongitud destino = UnidadLongitud.valueOf(unidadFinal.toUpperCase());
        return servicioLongitud.conversor(valor, origen, destino);
    }
    
    /**
     * Conversion de unidades de masa
     */
    @WebMethod(operationName = "convertirMasa")
    public double convertirMasa(
            @WebParam(name = "valor") double valor,
            @WebParam(name = "unidadInicial") String unidadInicial,
            @WebParam(name = "unidadFinal") String unidadFinal) {

        UnidadMasa origen = UnidadMasa.valueOf(unidadInicial.toUpperCase());
        UnidadMasa destino = UnidadMasa.valueOf(unidadFinal.toUpperCase());
        return servicioMasa.conversor(valor, origen, destino);
    }
    
    /**
     * Conversion de unidades de temperatura
     */
    @WebMethod(operationName = "convertirTemperatura")
    public double convertirTemperatura(
            @WebParam(name = "valor") double valor,
            @WebParam(name = "unidadInicial") String unidadInicial,
            @WebParam(name = "unidadFinal") String unidadFinal) {

        UnidadTemperatura origen = UnidadTemperatura.valueOf(unidadInicial.toUpperCase());
        UnidadTemperatura destino = UnidadTemperatura.valueOf(unidadFinal.toUpperCase());
        return servicioTemperatura.conversor(valor, origen, destino);
    }
    
}
