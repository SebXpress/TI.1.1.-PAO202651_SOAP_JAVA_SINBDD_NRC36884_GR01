/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controladores.manejadoresws;

import ec.edu.monster.seguridad.AdministradorTokens;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.NodeList;


/**
 *
 * @author sebas
 */
public class ManejadorAutenticacion implements SOAPHandler<SOAPMessageContext>{
    
    @Override
    public boolean handleMessage(SOAPMessageContext contexto) {
         Boolean esSalida = (Boolean) contexto.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
                 
        // interseptamos peticiones entrantes del cliente al servidor
        if(!esSalida){
            try {
                // obtener el nombre de la operacion WSDL-llamada
                QName operacion = (QName) contexto.get(MessageContext.WSDL_OPERATION);
                
                // si la operacion es login no requiere token previo
                if(operacion != null && "login".equals(operacion.getLocalPart())){
                    return true;
                }
                
                // para cualquier otra operacion, inspeccionamos el Header SOAP
                var mensajeSOAP = contexto.getMessage();
                var encabezado = mensajeSOAP.getSOAPHeader();
                
                if(encabezado == null){
                    throw new RuntimeException("Acceso denegado: Se requiere encabezado SOAP con el token de seguridad.");
                }
                
                // buscamos el nodo <token> dentro de la cabecera XML
                NodeList nodosToken = encabezado.getElementsByTagNameNS("*", "token");
                if(nodosToken.getLength() == 0){
                    throw new RuntimeException("Acceso denegado: No se encontro el token. Inicie sesión primero.");
                }
                
                String token = nodosToken.item(0).getTextContent();
                
                // validamos el token contra nuestro administrador de tokens
                if(!AdministradorTokens.validarToken(token)){
                    throw new RuntimeException("Acceso denegado: Token invalido o expirado.");
                }
                
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        // continuamos con la ejecucion normal del servicio con el true
        return true;
    }
    
    
    @Override
    public Set<QName> getHeaders() {
        return null;
    }


    @Override
    public boolean handleFault(SOAPMessageContext contexto) {
        return true;
    }

    @Override
    public void close(MessageContext contexto) {
    }

}