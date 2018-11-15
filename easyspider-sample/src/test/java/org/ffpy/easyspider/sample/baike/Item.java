package org.ffpy.easyspider.sample.baike;

public class Item {
    private String name;
    private String intro;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }
}
