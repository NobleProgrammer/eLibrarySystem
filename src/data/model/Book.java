/*
Book Class is implemented here
*/
package data.model;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {
    private String name;
    private String author;
    private String category;
    private int publishYear;
    private Date additionDate;
    private double buyPrice;
    private double rentPrice;
    private static int bookCount;
    private int bookID;

    public Book(String name, String author, String category, int publishYear, Date additionDate, double buyPrice, double rentPrice) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.publishYear = publishYear;
        this.additionDate = additionDate;
        this.buyPrice = buyPrice;
        this.rentPrice = rentPrice;
        this.bookCount++;
        this.bookID = bookCount;
    }

    public static int getBookCount() {
        return bookCount;
    }

    public int getBookID() {
        return bookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public Date getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(Date additionDate) {
        this.additionDate = additionDate;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }    
}
