package com.magicteam.magic;

import java.util.Date;

public class Deal extends DealId {

    public String startingDate;
    public String endingDate;
    public String discountCode;
    public String image;
    public String link;
    public String thumb;
    public String title;
    public String userId;
    public Date timestamp;
//    public Float price, normalPrice, shippingCost;
    public String price, normalPrice, shippingCost;
    public String categories;
    public String description;
    public String score;

    public Deal() {

    }

    public Deal(String startingDate, String endingDate, String discountCode, String score, String image, String link, String thumb, String title, String userId, Date timestamp, String price, String normalPrice, String shippingCost, String categories, String description) {
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.discountCode = discountCode;
        this.image = image;
        this.link = link;
        this.thumb = thumb;
        this.title = title;
        this.userId = userId;
        this.timestamp = timestamp;
        this.price = price;
        this.normalPrice = normalPrice;
        this.shippingCost = shippingCost;
        this.categories = categories;
        this.description = description;
        this.score = score;
    }

    public Deal(String image, String thumb, String title, String price, String categories, String description) { // mandatory fields
        this.image = image;
        this.thumb = thumb;
        this.title = title;
        this.price = price;
        this.categories = categories;
        this.description = description;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(String normalPrice) {
        this.normalPrice = normalPrice;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
