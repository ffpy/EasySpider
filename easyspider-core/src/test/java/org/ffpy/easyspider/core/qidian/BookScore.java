package org.ffpy.easyspider.core.qidian;

public class BookScore {
    private String value;
    private String count;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BookScore{" +
                "value='" + value + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
