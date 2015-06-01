
import auction.web.*;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Alexander
 */
public class AuctionMain {
    private static final String url = "http://localhost:5159/Auction";
    private static final String url2 = "http://localhost:5159/Registration";

    public static void main(String[] args) {
        Endpoint.publish(url, new Auction());
        Endpoint.publish(url2, new Registration());
    }
}
