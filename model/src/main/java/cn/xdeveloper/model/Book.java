package cn.xdeveloper.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by lai_book on 2017/6/9.
 */
@RealmClass
public class Book implements Parcelable, RealmModel {

    /**
     * id
     */
    @PrimaryKey
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 价格
     */
    private int price;

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeInt(this.price);
    }

    public void readFromParcel(Parcel in){
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.desc = in.readString();
        this.price = in.readInt();
    }


    protected Book(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.desc = in.readString();
        this.price = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
