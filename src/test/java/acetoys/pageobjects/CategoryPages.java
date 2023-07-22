package acetoys.pageobjects;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class CategoryPages {
    private static final FeederBuilder<String> categoryFeeder =
            csv("data/categoryDetails.csv").random();
    /* Read from categoryDetails.csv >> categoryName, categorySlug columns */
    public static ChainBuilder productListByCategory =
            feed(categoryFeeder)
                    .exec(
                            http("Load Products List page: #{categoryName}")
                                    .get("/category/#{categorySlug}")
                                    .check(css("#CategoryName").isEL("#{categoryName}"))
                    );
    public static ChainBuilder cyclePagesOfProducts =
            exec(session -> {
                int currentPageNumber = session.getInt("productsListPageNumber");
                int totalExpectedPages = session.getInt("categoryPages");
                boolean morePagesToCycleThrough = currentPageNumber < totalExpectedPages;
                System.out.println("morePagesToCycleThrough = " + morePagesToCycleThrough);
                return session.setAll(Map.of(
                        "currentPageNumber", currentPageNumber,
                        "nextPageNumber", currentPageNumber + 1,
                        "morePagesToCycleThrough", morePagesToCycleThrough));

            }).asLongAs("#{morePagesToCycleThrough}")
                    .on(exec(http("Load page #{currentPageNumber} of Products - Category: #{categoryName}")
                            .get("/category/#{categorySlug}?page=#{currentPageNumber}")
                            .check(css(".page-item.active").isEL("#{nextPageNumber}")))
                            .exec(session -> {
                                int currentPageNumber = session.getInt("currentPageNumber");
                                int totalExpectedPages = session.getInt("categoryPages");
                                currentPageNumber++;
                                boolean morePagesToCycleThrough = currentPageNumber < totalExpectedPages;
                                return session.setAll(Map.of(
                                        "currentPageNumber", currentPageNumber,
                                        "nextPageNumber", (currentPageNumber + 1),
                                        "morePagesToCycleThrough", morePagesToCycleThrough));
                            })
                    );
}
