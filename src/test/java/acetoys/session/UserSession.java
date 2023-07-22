package acetoys.session;

import io.gatling.javaapi.core.ChainBuilder;

import java.text.DecimalFormat;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.flushCookieJar;

public class UserSession {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static ChainBuilder initSession =
            exec(flushCookieJar())
                    .exec(session -> session.set("productsListPageNumber", 1))
                    .exec(session -> session.set("customerLoggedIn", false))
                    .exec(session -> session.set("itemsInBasket", 0))
                    .exec(session -> session.set("basketTotal", 0));
    public static ChainBuilder increaseItemsInBasketForSession =
            exec(session -> {
                int itemsInBasket = session.getInt("itemsInBasket");
                return session.set("itemsInBasket", (itemsInBasket + 1));
            });
    public static ChainBuilder decreaseItemsInBasketForSession =
            exec(session -> {
                int itemsInBasket = session.getInt("itemsInBasket");
                return session.set("itemsInBasket", (itemsInBasket - 1));
            });
    public static ChainBuilder increaseSessionBasketTotal =
            exec(session -> {
                double currentBasketTotal = session.getDouble("basketTotal");
                double itemPrice  = session.getDouble("price"); //read from productDetails.json
                double updatedBasketTotal = currentBasketTotal + itemPrice;
                return session.set("basketTotal", df.format(updatedBasketTotal));
            });
    public static ChainBuilder decreaseSessionBasketTotal =
            exec(session -> {
                double currentBasketTotal = session.getDouble("basketTotal");
                double itemPrice  = session.getDouble("price"); //read from productDetails.json
                double updatedBasketTotal = currentBasketTotal - itemPrice;
                return session.set("basketTotal", df.format(updatedBasketTotal));
            });
}
