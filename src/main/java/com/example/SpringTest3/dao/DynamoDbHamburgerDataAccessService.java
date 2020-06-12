package com.example.SpringTest3.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.example.SpringTest3.model.Hamburger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("dynamodbDao")
public class DynamoDbHamburgerDataAccessService implements HamburgerDao {

    AmazonDynamoDB client;
    DynamoDB dynamoDB;

    public DynamoDbHamburgerDataAccessService() {
        client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build();

        dynamoDB = new DynamoDB(client);
    }

    @Override
    public int insertHamburger(UUID id, Hamburger hamburger) {
        Table table = dynamoDB.getTable("Hamburgers");

        String stringId = id.toString();

        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("description", hamburger.getDescription());
        infoMap.put("price", hamburger.getPrice());

        try {
            System.out.println("Adding a new item...");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("id", stringId).withMap("info", infoMap));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        }
        catch (Exception e) {
            System.err.println("Unable to add item: " + id);
            System.err.println(e.getMessage());
            return 0;
        }

        return 1;
    }

    @Override
    public int updateHamburguerById(UUID id, Hamburger hamburger) {
        Table table = dynamoDB.getTable("Hamburgers");

        String stringId = id.toString();

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("id", stringId)
                .withUpdateExpression("set info.name = :n, info.description = :d, info.price=:p")
                .withValueMap(new ValueMap()
                        .withString(":n", hamburger.getName())
                        .withString(":d", hamburger.getDescription())
                        .withNumber(":p", hamburger.getPrice()))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

        }
        catch (Exception e) {
            System.err.println("Unable to update item: " + id);
            System.err.println(e.getMessage());
            return 0;
        }
        return 1;
    }

    @Override
    public int deleteHamburgerById(UUID id) {
        Table table = dynamoDB.getTable("Hamburgers");

        String stringId = id.toString();


        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("id", stringId));

        // Conditional delete (we expect this to fail)

        try {
            System.out.println("Attempting a conditional delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete item: " + id);
            System.err.println(e.getMessage());
            return 0;
        }
        return 1;
    }

    @Override
    public List<Hamburger> selectAllHamburgers() {
        List<Hamburger> hamburgers = new ArrayList<>();

        Table table = dynamoDB.getTable("Hamburgers");

        ScanSpec scanSpec = new ScanSpec().withProjectionExpression("id, info");

        try {
            ItemCollection<ScanOutcome> items = table.scan(scanSpec);

            Iterator<Item> iter = items.iterator();
            while (iter.hasNext()) {
                Item item = iter.next();
                System.out.println(item.toString());
                Hamburger hamburger = new Hamburger(UUID.fromString(String.valueOf(item.get("id"))),String.valueOf(item.get("info.name")),
                        String.valueOf(item.get("info.description")),Double.parseDouble(String.valueOf(item.get("info.price"))));
                hamburgers.add(hamburger);
            }

        }
        catch (Exception e) {
            System.err.println("Unable to scan the table:");
            System.err.println(e.getMessage());
        }
        return hamburgers;
    }

    @Override
    public Optional<Hamburger> selectHamburgerById(UUID id) {
        Table table = dynamoDB.getTable("Hamburgers");

        String stringId = id.toString();

        GetItemSpec spec = new GetItemSpec().withPrimaryKey("id", stringId);

        try {
            System.out.println("Attempting to read the item...");
            Item item = table.getItem(spec);
            System.out.println("GetItem succeeded: " + item);
            Hamburger hamburger = new Hamburger(UUID.fromString(String.valueOf(item.get("id"))),String.valueOf(item.get("info.name")),
                    String.valueOf(item.get("info.description")),Double.parseDouble(String.valueOf(item.get("info.price"))));
            return Optional.of(hamburger);
        }
        catch (Exception e) {
            System.err.println("Unable to read item: " + id);
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }
}
