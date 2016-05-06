
package sk.tuke.gamestudio.webservice.favorites;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sk.tuke.gamestudio.webservice.favorites package. 
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

    private final static QName _AddFavorite1_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "addFavorite1");
    private final static QName _AddFavorite2_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "addFavorite2");
    private final static QName _AddFavorite1Response_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "addFavorite1Response");
    private final static QName _RemoveFavorite_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "removeFavorite");
    private final static QName _RemoveFavoriteResponse_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "removeFavoriteResponse");
    private final static QName _AddFavorite2Response_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "addFavorite2Response");
    private final static QName _GetFavoritesResponse_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "getFavoritesResponse");
    private final static QName _GetFavorites_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "getFavorites");
    private final static QName _FavoriteException_QNAME = new QName("http://favorites.webservice.gamestudio.tuke.sk/", "FavoriteException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sk.tuke.gamestudio.webservice.favorites
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RemoveFavoriteResponse }
     * 
     */
    public RemoveFavoriteResponse createRemoveFavoriteResponse() {
        return new RemoveFavoriteResponse();
    }

    /**
     * Create an instance of {@link FavoriteException }
     * 
     */
    public FavoriteException createFavoriteException() {
        return new FavoriteException();
    }

    /**
     * Create an instance of {@link GetFavorites }
     * 
     */
    public GetFavorites createGetFavorites() {
        return new GetFavorites();
    }

    /**
     * Create an instance of {@link AddFavorite2Response }
     * 
     */
    public AddFavorite2Response createAddFavorite2Response() {
        return new AddFavorite2Response();
    }

    /**
     * Create an instance of {@link GetFavoritesResponse }
     * 
     */
    public GetFavoritesResponse createGetFavoritesResponse() {
        return new GetFavoritesResponse();
    }

    /**
     * Create an instance of {@link RemoveFavorite }
     * 
     */
    public RemoveFavorite createRemoveFavorite() {
        return new RemoveFavorite();
    }

    /**
     * Create an instance of {@link AddFavorite1Response }
     * 
     */
    public AddFavorite1Response createAddFavorite1Response() {
        return new AddFavorite1Response();
    }

    /**
     * Create an instance of {@link AddFavorite2 }
     * 
     */
    public AddFavorite2 createAddFavorite2() {
        return new AddFavorite2();
    }

    /**
     * Create an instance of {@link AddFavorite1 }
     * 
     */
    public AddFavorite1 createAddFavorite1() {
        return new AddFavorite1();
    }

    /**
     * Create an instance of {@link FavoriteGameEntity }
     * 
     */
    public FavoriteGameEntity createFavoriteGameEntity() {
        return new FavoriteGameEntity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFavorite1 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "addFavorite1")
    public JAXBElement<AddFavorite1> createAddFavorite1(AddFavorite1 value) {
        return new JAXBElement<AddFavorite1>(_AddFavorite1_QNAME, AddFavorite1 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFavorite2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "addFavorite2")
    public JAXBElement<AddFavorite2> createAddFavorite2(AddFavorite2 value) {
        return new JAXBElement<AddFavorite2>(_AddFavorite2_QNAME, AddFavorite2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFavorite1Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "addFavorite1Response")
    public JAXBElement<AddFavorite1Response> createAddFavorite1Response(AddFavorite1Response value) {
        return new JAXBElement<AddFavorite1Response>(_AddFavorite1Response_QNAME, AddFavorite1Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveFavorite }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "removeFavorite")
    public JAXBElement<RemoveFavorite> createRemoveFavorite(RemoveFavorite value) {
        return new JAXBElement<RemoveFavorite>(_RemoveFavorite_QNAME, RemoveFavorite.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveFavoriteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "removeFavoriteResponse")
    public JAXBElement<RemoveFavoriteResponse> createRemoveFavoriteResponse(RemoveFavoriteResponse value) {
        return new JAXBElement<RemoveFavoriteResponse>(_RemoveFavoriteResponse_QNAME, RemoveFavoriteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFavorite2Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "addFavorite2Response")
    public JAXBElement<AddFavorite2Response> createAddFavorite2Response(AddFavorite2Response value) {
        return new JAXBElement<AddFavorite2Response>(_AddFavorite2Response_QNAME, AddFavorite2Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFavoritesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "getFavoritesResponse")
    public JAXBElement<GetFavoritesResponse> createGetFavoritesResponse(GetFavoritesResponse value) {
        return new JAXBElement<GetFavoritesResponse>(_GetFavoritesResponse_QNAME, GetFavoritesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFavorites }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "getFavorites")
    public JAXBElement<GetFavorites> createGetFavorites(GetFavorites value) {
        return new JAXBElement<GetFavorites>(_GetFavorites_QNAME, GetFavorites.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FavoriteException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://favorites.webservice.gamestudio.tuke.sk/", name = "FavoriteException")
    public JAXBElement<FavoriteException> createFavoriteException(FavoriteException value) {
        return new JAXBElement<FavoriteException>(_FavoriteException_QNAME, FavoriteException.class, null, value);
    }

}
