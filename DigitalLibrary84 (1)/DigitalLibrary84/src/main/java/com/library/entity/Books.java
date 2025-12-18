package com.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bid;
    private String bname;
    private String bauth;
    private String pyear;
    private String bcategory;
    private String isbn;
    private String status;
  

    // New field for file storage
    private String fileUrl;

    public Books() {}

    public Books(int bid, String bname, String bauth, String pyear, String bcategory, String isbn, String status, String fileUrl) {
        this.bid = bid;
        this.bname = bname;
        this.bauth = bauth;
        this.pyear = pyear;
        this.bcategory = bcategory;
        this.isbn = isbn;
        this.status = status;
        this.fileUrl = fileUrl;
    }

    // Getters and Setters
    public int getBid() {
        return bid;
    }
    public void setBid(int bid) {
        this.bid = bid;
    }
    public String getBname() {
        return bname;
    }
    public void setBname(String bname) {
        this.bname = bname;
    }
    public String getBauth() {
        return bauth;
    }
    public void setBauth(String bauth) {
        this.bauth = bauth;
    }
    public String getPyear() {
        return pyear;
    }
    public void setPyear(String pyear) {
        this.pyear = pyear;
    }
    public String getBcategory() {
        return bcategory;
    }
    public void setBcategory(String bcategory) {
        this.bcategory = bcategory;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getFileUrl() {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
