package cn.xdeveloper.book.stock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import cn.xdeveloper.model.Book;
import cn.xdeveloper.model.IBookManager;
import cn.xdeveloper.model.IOnStockCallback;
import io.realm.Realm;

/**
 * 远程Service
 * Created by lai_book on 2017/6/10.
 */
public class BookStockService extends Service {

    private static final String TAG = BookStockService.class.getName();

    //远程回调列表
    private RemoteCallbackList<IOnStockCallback> mCallBackList = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "---------onCreate--------");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "---------onBind--------");

        return mBinder;
    }

    private IBinder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> loadAll() throws RemoteException {
            Realm realm = Realm.getDefaultInstance();
            try {
                List<Book> bookList = realm.where(Book.class).findAll();
                Log.d(TAG, "loadAll:" + bookList.toString());
                return realm.copyFromRealm(bookList);
            } finally {
                realm.close();
            }
        }

        @Override
        public boolean delete(final long id) throws RemoteException {
            Realm realm = Realm.getDefaultInstance();

            boolean result = false;
            try {
                realm.beginTransaction();
                result = realm.where(Book.class).equalTo("id", id).findAll().deleteAllFromRealm();
                realm.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                realm.cancelTransaction();
            } finally {
                realm.close();
            }
            return result;
        }

        @Override
        public boolean addOrUpdate(final Book book) throws RemoteException {
            Realm realm = Realm.getDefaultInstance();
            boolean result = false;

            if (book.getId() == null) {
                book.setId(realm.where(Book.class).max("id").longValue() + 1);
            }

            try {
                realm.beginTransaction();
                realm.insertOrUpdate(book);
                realm.commitTransaction();
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                realm.cancelTransaction();
            } finally {
                realm.close();
            }
            return result;
        }

        @Override
        public void registerListener(IOnStockCallback callback) throws RemoteException {
            mCallBackList.register(callback);
        }

        @Override
        public void unregisterListener(IOnStockCallback callback) throws RemoteException {
            mCallBackList.unregister(callback);
        }


    };


    /**
     * 发送通知到书店
     * @param msg
     */
    private void notifyStore(String msg){
        int size = mCallBackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            IOnStockCallback callback = mCallBackList.getBroadcastItem(i);
            if(callback != null){
                try {
                    callback.onNotify(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mCallBackList.finishBroadcast();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "----------onDestroy-----------");
    }
}
