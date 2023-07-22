package acetoys.pageobjects;

import acetoys.session.UserSession;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ProductsPages {
    public static final FeederBuilder<Object> productFeeder =
            jsonFile("data/productDetails.json").random();

    public static ChainBuilder loadProductDetailsPage =
            feed(productFeeder)
                    .exec(
                            http("Load Products Details Page: #{name}")/* name property from productDetails.json */
                                    .get("/product/#{slug}")
                                    .check(css("#ProductDescription").isEL("#{description}"))
                    );
    public static ChainBuilder addProductToCart =
            exec(UserSession.increaseItemsInBasketForSession)
                    .exec(
                            http("Add Product to Cart - Product Name: #{name}")
                                    .get("/cart/add/#{id}")
                                    .check(substring("You have <span>#{itemsInBasket}</span> products in your Basket.")))
                    .exec(UserSession.increaseSessionBasketTotal);
}
