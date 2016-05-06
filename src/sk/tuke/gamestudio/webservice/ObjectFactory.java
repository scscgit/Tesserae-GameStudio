
package sk.tuke.gamestudio.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sk.tuke.gamestudio.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ScoreException_QNAME = new QName("http://webservice.gamestudio.tuke.sk/", "ScoreException");
    private final static QName _GetBestScoresForGame_QNAME = new QName("http://webservice.gamestudio.tuke.sk/", "getBestScoresForGame");
    private final static QName _AddScore_QNAME = new QName("http://webservice.gamestudio.tuke.sk/", "addScore");
    private final static QName _GetBestScoresForGameResponse_QNAME = new QName("http://webservice.gamestudio.tuke.sk/", "getBestScoresForGameResponse");
    private final static QName _AddScoreResponse_QNAME = new QName("http://webservice.gamestudio.tuke.sk/", "addScoreResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sk.tuke.gamestudio.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ScoreException }
     * 
     */
    public ScoreException createScoreException() {
        return new ScoreException();
    }

    /**
     * Create an instance of {@link GetBestScoresForGame }
     * 
     */
    public GetBestScoresForGame createGetBestScoresForGame() {
        return new GetBestScoresForGame();
    }

    /**
     * Create an instance of {@link AddScore }
     * 
     */
    public AddScore createAddScore() {
        return new AddScore();
    }

    /**
     * Create an instance of {@link GetBestScoresForGameResponse }
     * 
     */
    public GetBestScoresForGameResponse createGetBestScoresForGameResponse() {
        return new GetBestScoresForGameResponse();
    }

    /**
     * Create an instance of {@link AddScoreResponse }
     * 
     */
    public AddScoreResponse createAddScoreResponse() {
        return new AddScoreResponse();
    }

    /**
     * Create an instance of {@link Score }
     * 
     */
    public Score createScore() {
        return new Score();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScoreException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gamestudio.tuke.sk/", name = "ScoreException")
    public JAXBElement<ScoreException> createScoreException(ScoreException value) {
        return new JAXBElement<ScoreException>(_ScoreException_QNAME, ScoreException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBestScoresForGame }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gamestudio.tuke.sk/", name = "getBestScoresForGame")
    public JAXBElement<GetBestScoresForGame> createGetBestScoresForGame(GetBestScoresForGame value) {
        return new JAXBElement<GetBestScoresForGame>(_GetBestScoresForGame_QNAME, GetBestScoresForGame.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddScore }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gamestudio.tuke.sk/", name = "addScore")
    public JAXBElement<AddScore> createAddScore(AddScore value) {
        return new JAXBElement<AddScore>(_AddScore_QNAME, AddScore.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBestScoresForGameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gamestudio.tuke.sk/", name = "getBestScoresForGameResponse")
    public JAXBElement<GetBestScoresForGameResponse> createGetBestScoresForGameResponse(GetBestScoresForGameResponse value) {
        return new JAXBElement<GetBestScoresForGameResponse>(_GetBestScoresForGameResponse_QNAME, GetBestScoresForGameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddScoreResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.gamestudio.tuke.sk/", name = "addScoreResponse")
    public JAXBElement<AddScoreResponse> createAddScoreResponse(AddScoreResponse value) {
        return new JAXBElement<AddScoreResponse>(_AddScoreResponse_QNAME, AddScoreResponse.class, null, value);
    }

}
