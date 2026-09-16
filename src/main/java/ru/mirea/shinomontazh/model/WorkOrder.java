package ru.mirea.shinomontazh.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WorkOrder {
    private int id;
    private String carBrand;
    private String carNumber;
    private String serviceName;
    private OrderStatus status;
    private BigDecimal price;
    private LocalDateTime createDate;
    private int clientId;

    public WorkOrder(){

    }

    public WorkOrder(
        int id,
        String carBrand,
        String carNumber,
        String serviceName,
        OrderStatus status,
        BigDecimal price,
        LocalDateTime createDate,
        int clientId
    ){
        this.id = id;
        this.carBrand = carBrand;
        this.carNumber = carNumber;
        this.serviceName = serviceName;
        this.status = status;
        this.price = price;
        this.createDate = createDate;
        this.clientId = clientId;
    }
    public int getId(){
        return id;
    }
    public String getCarBrand(){
        return carBrand;
    }

    public String getCarNumber(){
        return carNumber;
    }

    public String getServiceName(){
        return serviceName;
    }

    public OrderStatus getStatus(){
        return status;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public LocalDateTime getCreateDate(){
        return createDate;
    }

    public int getClientId(){
        return clientId;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setCarBrand(String carBrand){
        this.carBrand = carBrand;
    }
    public void setCarNumber(String carNumber){
        this.carNumber = carNumber;
    }
    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
    }

    public void setStatus(OrderStatus status){
        this.status = status;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public void setCreateDate(LocalDateTime createDate){
        this.createDate = createDate;
    }

    public void setClientId(int clientId){
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "WorkOrder{" +
                "id=" + id +
                ", carBrand='" + carBrand + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", status=" + status +
                ", price=" + price +
                ", createDate=" + createDate +
                ", clientId=" + clientId +
                '}';
    }
}