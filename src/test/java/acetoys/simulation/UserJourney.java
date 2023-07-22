package acetoys.simulation;

import acetoys.pageobjects.*;
import io.gatling.javaapi.core.ChainBuilder;

import java.time.Duration;

import static acetoys.session.UserSession.initSession;
import static io.gatling.javaapi.core.CoreDsl.exec;

public class UserJourney {
    private static final Duration LOW_PAUSE = Duration.ofMillis(1000);
    private static final Duration HIGH_PAUSE = Duration.ofMillis(3000);
    public static ChainBuilder browseStore =
            exec(initSession)
                    .exec(StaticPages.homePage)
                    .pause(HIGH_PAUSE)
                    .exec(StaticPages.ourStoryPage)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .exec(StaticPages.getInTouchPage)
                    .pause(LOW_PAUSE, HIGH_PAUSE)
                    .repeat(3).on(
                            exec(CategoryPages.productListByCategory)
                                    .pause(LOW_PAUSE, HIGH_PAUSE)
                                    .exec(CategoryPages.cyclePagesOfProducts)
                                    .pause(LOW_PAUSE, HIGH_PAUSE)
                                    .exec(ProductsPages.loadProductDetailsPage)
                    );
    public static ChainBuilder abandonBasket =
            exec(initSession)
                    .exec(StaticPages.homePage)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(CategoryPages.productListByCategory)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(ProductsPages.loadProductDetailsPage)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(ProductsPages.addProductToCart);

    public static ChainBuilder completePurchase =
            exec(initSession)
                    .exec(StaticPages.homePage)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(CategoryPages.productListByCategory)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(ProductsPages.loadProductDetailsPage)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(ProductsPages.addProductToCart)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(CartPage.viewCart)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(CartPage.increaseQuantityInCart)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(CartPage.decreaseQuantityInCart)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(CartPage.checkout)
                    .pace(LOW_PAUSE, HIGH_PAUSE)
                    .exec(CustomerPages.logout);

}
