
package sk.tuke.gamestudio.webservice;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.7-b01 
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "ScoreException", targetNamespace = "http://webservice.gamestudio.tuke.sk/")
public class ScoreException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ScoreException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public ScoreException_Exception(String message, ScoreException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public ScoreException_Exception(String message, ScoreException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: sk.tuke.gamestudio.webservice.ScoreException
     */
    public ScoreException getFaultInfo() {
        return faultInfo;
    }

}
