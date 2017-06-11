package cn.xdeveloper.book.stock;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by lai_book on 2017/6/10.
 */

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name("BookStock.realm")
                .initialData(new DataInitial())
                .build();

        Realm.setDefaultConfiguration(config);
    }
}
