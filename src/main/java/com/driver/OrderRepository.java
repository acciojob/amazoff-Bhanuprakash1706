package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderInDB=new HashMap<>();
    HashMap<DeliveryPartner, LinkedList<String>> deliveryPartnerInDB=new HashMap<>();

    public void addOrder(Order order) {
        orderInDB.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner=new DeliveryPartner(partnerId);
        LinkedList<String> list=new LinkedList<>();
        deliveryPartnerInDB.put(deliveryPartner,list);
    }
    public void addOrderPartnerPair(String orderId, String partnerId) {
                for(DeliveryPartner dp:deliveryPartnerInDB.keySet()){
                    if (Objects.equals(partnerId,dp.getId())){
                        dp.setNumberOfOrders(dp.getNumberOfOrders()+1);
                        LinkedList<String> list=deliveryPartnerInDB.get(dp);
                        list.add(orderId);
                        deliveryPartnerInDB.put(dp,list);
                        return;
            }
        }
    }
    public Order getOrderById(String orderId) {
        return orderInDB.get(orderId);

    }
    public DeliveryPartner getPartnerById(String partnerId) {
        for (DeliveryPartner dp:deliveryPartnerInDB.keySet()){
            if(Objects.equals(partnerId,dp.getId())){
                return dp;
            }
        }
        return null;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        for(DeliveryPartner dp:deliveryPartnerInDB.keySet()){
            if(Objects.equals(partnerId,dp.getId())){
                return dp.getNumberOfOrders();
            }
        }
        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {

        for(DeliveryPartner dp:deliveryPartnerInDB.keySet()){
            if(Objects.equals(partnerId,dp.getId())){
                return deliveryPartnerInDB.get(dp);
            }
        }
        return null;
    }

    public List<String> getAllOrders() {
        List<String> list=new LinkedList<>();
        list.addAll(orderInDB.keySet());
        return list;
    }

    public Integer getCountOfUnassignedOrders() {
       int count=0;
       HashMap<String,Integer> map=new HashMap<>();
           for(List<String> list:deliveryPartnerInDB.values()){
               for(String order:list){
                    map.put(order,1);
               }
           }
           for (String str:orderInDB.keySet()){
               if(!map.containsKey(str))
                   count++;
       }
        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId) {
        int count=0;
        int currTime=Integer.parseInt(time.substring(0,2))*60+Integer.parseInt(time.substring(3,5));
        List<String> list=new LinkedList<>();
        for (DeliveryPartner dp:deliveryPartnerInDB.keySet()){
            if(Objects.equals(partnerId,dp.getId())){
                list=deliveryPartnerInDB.get(dp);
                break;
            }
        }
        for (String orderName:list){
            Order order=orderInDB.get(orderName);
            if (currTime>order.getDeliveryTime()){
                count++;
            }
        }
        return  count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String hh="",mm="",zero="0";
        for (DeliveryPartner dp:deliveryPartnerInDB.keySet()){
            if (Objects.equals(dp.getId(),partnerId)){
                String str=deliveryPartnerInDB.get(dp).getLast();
               int temp= orderInDB.get(str).getDeliveryTime();
               hh= String.valueOf((temp/60<10)?(zero+temp/60):temp/60);
               mm= String.valueOf((temp%60<10)?(zero+temp%60):temp%60);

            }
        }
        return hh+":"+mm;
    }

    public void deletePartnerById(String partnerId) {
        List<String> list=new LinkedList<>();
        for (DeliveryPartner dp:deliveryPartnerInDB.keySet()){
            if (Objects.equals(dp.getId(),partnerId)){
                list=deliveryPartnerInDB.get(dp);
                deliveryPartnerInDB.remove(dp);
                break;
            }
        }
        for (String order:list){
            orderInDB.remove(order);
        }
    }

    public void deleteOrderById(String orderId) {
        orderInDB.remove(orderId);
        for (DeliveryPartner dp:deliveryPartnerInDB.keySet()){
            LinkedList<String> orderList=deliveryPartnerInDB.get(dp);
            if (orderList.remove(orderId)){
                deliveryPartnerInDB.put(dp,orderList);
                return;
            }
        }
    }



}
