// IStoreManager.aidl
package cn.xdeveloper.model;

// Declare any non-default types here with import statements
import cn.xdeveloper.model.Book;
import cn.xdeveloper.model.IOnStockCallback;

interface IBookManager {

    //加载所有
    List<Book> loadAll();

    //删除
    boolean delete(long id);

    //添加或更新
    boolean addOrUpdate(inout Book book);

    //注册回调
    void registerListener(IOnStockCallback callback);

    //解除回调
    void unregisterListener(IOnStockCallback callback);
}
