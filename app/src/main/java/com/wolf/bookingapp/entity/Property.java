package com.wolf.bookingapp.entity;

import java.io.Serializable;
import java.util.List;

public class Property implements Serializable {
    private int id;
    private String name;
    private int adultCapacity;
    private int childrenCapacity;
    private boolean petsAllowed;
    private String address;
    private Double price;
    private Double discount;
    private int star;
    private int singleBed;
    private int doubleBed;
    private int bedRoom;
    private int quantity;
    private String type;
    private List<String> listImage;

    private List<Comment> listComment;

    private String regionName;

    public Property() {
    }

    public Property(int id, String name, int adultCapacity, int childrenCapacity, boolean petsAllowed, String address, Double price, Double discount, int star, int singleBed, int doubleBed, int bedRoom, int quantity, String type, List<String> listImage) {
        this.id = id;
        this.name = name;
        this.adultCapacity = adultCapacity;
        this.childrenCapacity = childrenCapacity;
        this.petsAllowed = petsAllowed;
        this.address = address;
        this.price = price;
        this.discount = discount;
        this.star = star;
        this.singleBed = singleBed;
        this.doubleBed = doubleBed;
        this.bedRoom = bedRoom;
        this.quantity = quantity;
        this.type = type;
        this.listImage = listImage;
    }

    public Property(int id, String name, int adultCapacity, int childrenCapacity, boolean petsAllowed, String address, Double price, Double discount, int star, int singleBed, int doubleBed, int bedRoom, int quantity, List<String> listImage, List<Comment> listComment, String type, String regionName) {
        this.id = id;
        this.name = name;
        this.adultCapacity = adultCapacity;
        this.childrenCapacity = childrenCapacity;
        this.petsAllowed = petsAllowed;
        this.address = address;
        this.price = price;
        this.discount = discount;
        this.star = star;
        this.singleBed = singleBed;
        this.doubleBed = doubleBed;
        this.bedRoom = bedRoom;
        this.quantity = quantity;
        this.listImage = listImage;
        this.listComment = listComment;
        this.type = type;
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<Comment> getListComment() {
        return listComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.listComment = listComment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdultCapacity() {
        return adultCapacity;
    }

    public void setAdultCapacity(int adultCapacity) {
        this.adultCapacity = adultCapacity;
    }

    public int getChildrenCapacity() {
        return childrenCapacity;
    }

    public void setChildrenCapacity(int childrenCapacity) {
        this.childrenCapacity = childrenCapacity;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getSingleBed() {
        return singleBed;
    }

    public void setSingleBed(int singleBed) {
        this.singleBed = singleBed;
    }

    public int getDoubleBed() {
        return doubleBed;
    }

    public void setDoubleBed(int doubleBed) {
        this.doubleBed = doubleBed;
    }

    public int getBedRoom() {
        return bedRoom;
    }

    public void setBedRoom(int bedRoom) {
        this.bedRoom = bedRoom;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
