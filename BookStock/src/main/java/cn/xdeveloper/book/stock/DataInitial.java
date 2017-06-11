package cn.xdeveloper.book.stock;

import java.util.ArrayList;
import java.util.List;

import cn.xdeveloper.model.Book;
import io.realm.Realm;

/**
 * 数据初始化类
 * Created by lai_book on 2017/4/25.
 */

public class DataInitial implements Realm.Transaction {


    @Override
    public void execute(Realm realm) {
        List<Book> list = new ArrayList<>();

        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("JAVA编程思想");
        book1.setDesc("这是一本JAVA进阶的好书");
        book1.setPrice(100);
        list.add(book1);


        realm.copyToRealm(list);
    }
}
