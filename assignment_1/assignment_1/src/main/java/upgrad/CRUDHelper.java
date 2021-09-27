package upgrad;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import static com.mongodb.client.model.Aggregates.count;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class CRUDHelper {

    /**
     * Display ALl products
     * @param collection
     */
    public static void displayAllProducts(MongoCollection<Document> collection) {
        System.out.println("------ Displaying All Products ------");
//        // Call printSingleCommonAttributes to display the attributes on the Screen
        for (Document document : collection.find().projection(include("_id","ProductId","Category","Manufacturer","Title"))) {
            PrintHelper.printAllAttributes(document);
        }
    }

    /**
     * Display top 5 Mobiles
     * @param collection
     */
    public static void displayTop5Mobiles(MongoCollection<Document> collection) {
        System.out.println("------ Displaying Top 5 Mobiles ------");
        Bson filter = eq("Category", "Mobile");
        // Call printAllAttributes to display the attributes on the Screen
        for (Document document : collection.find(filter).limit(5)) {
            PrintHelper.printAllAttributes(document);
        }
    }


    /**
     * Display products ordered by their categories in Descending order without auto generated Id
     * @param collection
     */
    public static void displayCategoryOrderedProductsDescending(MongoCollection<Document> collection) {
        System.out.println("------ Displaying Products ordered by categories ------");
        // Call printAllAttributes to display the attributes on the Screen
        for (Document document : collection.find().projection(excludeId()).sort(descending("Category"))) {
            PrintHelper.printAllAttributes(document);

        }
    }

    /**
     * Display number of products in each group
     * @param collection
     */
    public static void displayProductCountByCategory(MongoCollection<Document> collection) {
        System.out.println("------ Displaying Product Count by categories ------");
        // Call printProductCountInCategory to display the attributes on the Screen

        MongoCursor<Document> cursor = collection.aggregate(
                Arrays.asList(
                        Aggregates.group("$Category",
                                Accumulators.sum("Count",1)
                ))).cursor();
        while(cursor.hasNext()) {
            PrintHelper.printProductCountInCategory(cursor.next());
        }
    }

    /**
     * Display Wired Headphones
     * @param collection
     */
    public static void displayWiredHeadphones(MongoCollection<Document> collection) {
        System.out.println("------ Displaying Wired headphones ------");
        // Call printAllAttributes to display the attributes on the Screen

        Bson filter =eq("ConnectorType","Wired");
        for (Document document : collection.find(filter)) {
            PrintHelper.printAllAttributes(document);
        }
    }
}