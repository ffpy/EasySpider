package org.ffpy.easyspider.sample.qidian;

import java.util.Date;
import java.util.List;

public class BookInfo {
    private String name;
    private String author;
    private String intro;
    private List<BookCatalog> catalogs;
    private BookScore score;
    private Fans fans;
    private Date testDate;

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    public List<BookCatalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<BookCatalog> catalogs) {
        this.catalogs = catalogs;
    }

    public BookScore getScore() {
        return score;
    }

    public void setScore(BookScore score) {
        this.score = score;
    }

    public Fans getFans() {
        return fans;
    }

    public void setFans(Fans fans) {
        this.fans = fans;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", intro='" + intro + '\'' +
                ", catalogs=" + catalogs +
                ", score=" + score +
                ", fans=" + fans +
                ", testDate=" + testDate +
                '}';
    }
}
