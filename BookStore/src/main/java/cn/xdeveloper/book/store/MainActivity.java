package cn.xdeveloper.book.store;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import cn.xdeveloper.model.Book;
import cn.xdeveloper.model.IBookManager;
import cn.xdeveloper.model.IOnStockCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();

    private BookAdapter mBookAdapter;
    private IBookManager mBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        bindBookStock();
    }

    private void initView() {
        findViewById(R.id.tv_add).setOnClickListener(this);

        RecyclerView bookList = (RecyclerView) findViewById(R.id.bookList);
        bookList.setLayoutManager(new LinearLayoutManager(this));
        bookList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        bookList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Book book = (Book) adapter.getItem(position);

                Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                intent.putExtra("book", book);
                intent.putExtra("position", position);

                startActivityForResult(intent, 100);
            }
        });
        mBookAdapter = new BookAdapter(null);
        bookList.setAdapter(mBookAdapter);
    }

    /**
     * 绑定到远程Service
     */
    private void bindBookStock() {
        Intent intent = new Intent();
        intent.setPackage("cn.xdeveloper.book.stock");
        intent.setAction("cn.xdeveloper.book.stock.BookStockService");

        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBookManager != null) {
            try {
                mBookManager.unregisterListener(mOnStockCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(mServiceConnection);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                startActivityForResult(new Intent(this, BookDetailActivity.class), 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            //新增或修改

            Book book = data.getParcelableExtra("book");
            int position = data.getIntExtra("position", -1);

            try {
                boolean result = mBookManager.addOrUpdate(book);
                if (result) {
                    if (position == -1) {
                        mBookAdapter.addData(book);
                    } else {
                        mBookAdapter.remove(position);
                        mBookAdapter.addData(position, book);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "操作失败!", Toast.LENGTH_SHORT).show();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "操作失败!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 100 && resultCode == 201) {
            //删除

            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                Book book = mBookAdapter.getItem(position);
                try {
                    if (mBookManager.delete(book.getId())) {
                        mBookAdapter.remove(position);
                    } else {
                        Toast.makeText(MainActivity.this, "操作失败!", Toast.LENGTH_SHORT).show();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "操作失败!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBookManager = IBookManager.Stub.asInterface(service);
            try {
                //设置死亡代理
                service.linkToDeath(mDeathRecipient, 0);

                mBookAdapter.setNewData(mBookManager.loadAll());
                mBookManager.registerListener(mOnStockCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected:" + name);
        }
    };

    /**
     * 死亡代理
     * 当BookStock的binder死亡的时候可以得到回调，重绑定
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBookManager == null) return;

            mBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);

            //重新绑定
            bindBookStock();
        }
    };


    /**
     * Stock回调
     */
    private IOnStockCallback mOnStockCallback = new IOnStockCallback.Stub() {
        @Override
        public void onNotify(String msg) throws RemoteException {
            Log.d(TAG,"onNotify,msg:" + msg);
        }
    };

}
