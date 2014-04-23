package org.jbpm.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jbpm.example.data.Order;
import org.jbpm.example.data.ProductType;
import org.jbpm.example.service.exception.ForbiddenClientException;

public class WarehouseManagementSystem {

    private static Map<ProductType, Integer> storage = new HashMap<ProductType, Integer>() {
        {
            for (ProductType type : ProductType.values()) {
                put(type, 10); // storage contains 10 pieces of each product
                               // type
            }
        }
    };

    private static List<Integer> clientBlackList = new ArrayList<Integer>() {
        {
            for (int i = 10; i < 20; ++i) {
                add(i);
            }
        }
    };

    public void pickUp(Order order) throws Exception {
        Integer clientId = order.getClientId();
        if (clientId == null || clientBlackList.contains(clientId)) {
            throw new ForbiddenClientException("Client " + clientId + " is on the black list.");
        }
        ProductType type = ProductType.valueOf(order.getType());
        Integer count = storage.get(type) - order.getAmount();
        storage.put(type, count);
    }

    public boolean checkStockLevel(Order order) {
        if (order == null || order.getType() == null || order.getAmount() == null || order.getAmount() < 0) {
            return false;
        }

        ProductType type = ProductType.valueOf(order.getType());
        boolean available = (type != null && storage.get(type) - order.getAmount() >= 0);

        return available;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("WarehouseManagementSystem Storage Status\n");
        for (Entry<ProductType, Integer> item : storage.entrySet()) {
            sb.append(item.getKey().name());
            sb.append(" = ");
            sb.append(item.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

}
