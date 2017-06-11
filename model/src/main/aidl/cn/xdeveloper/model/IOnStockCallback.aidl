// IOnStockChangeListener.aidl
package cn.xdeveloper.model;

// Declare any non-default types here with import statements
import cn.xdeveloper.model.Book;

interface IOnStockCallback {

    //通知
    void onNotify(String msg);
}
