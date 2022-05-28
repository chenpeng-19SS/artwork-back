package com.fsy2001.artwork.model;

import javax.persistence.*;

@Entity
@Table(name = "artwork")
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String author;
    private Integer price;
    private Integer view;
    private boolean sold;
    private String imgPath;

    /* 参数 */
    private String detailYear;
    private String detailSize;
    private String detailGenre;
    private String detailEra;

    public Artwork() {
    }


    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getView() {
        return view;
    }

    public void addView() {
        view++;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDetailYear() {
        return detailYear;
    }

    public void setDetailYear(String detailYear) {
        this.detailYear = detailYear;
    }

    public String getDetailSize() {
        return detailSize;
    }

    public void setDetailSize(String detailSize) {
        this.detailSize = detailSize;
    }

    public String getDetailGenre() {
        return detailGenre;
    }

    public void setDetailGenre(String detailGenre) {
        this.detailGenre = detailGenre;
    }

    public String getDetailEra() {
        return detailEra;
    }

    public void setDetailEra(String detailEra) {
        this.detailEra = detailEra;
    }
}
