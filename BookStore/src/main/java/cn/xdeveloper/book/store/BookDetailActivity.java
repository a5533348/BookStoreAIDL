package cn.xdeveloper.book.store;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.xdeveloper.model.Book;

/**
 * 书籍详情
 * Created by lai_book on 2017/6/10.
 */

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_price;
    private EditText et_desc;

    private Book mBook;
    private int mPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mBook = getIntent().getParcelableExtra("book");
        mPosition = getIntent().getIntExtra("position", -1);

        et_name = (EditText) findViewById(R.id.et_name);
        et_price = (EditText) findViewById(R.id.et_price);
        et_desc = (EditText) findViewById(R.id.et_desc);
        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        if (mBook != null) {
            btn_add.setText("修改");
            TextView tv_del = (TextView) findViewById(R.id.tv_del);
            tv_del.setOnClickListener(this);
            tv_del.setVisibility(View.VISIBLE);

            et_name.setText(mBook.getName());
            et_price.setText(String.valueOf(mBook.getPrice()));
            et_desc.setText(mBook.getDesc());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:

                String name = et_name.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "请输入书名", Toast.LENGTH_SHORT).show();
                    return;
                }

                String price = et_price.getText().toString();
                if (TextUtils.isEmpty(price)) {
                    Toast.makeText(this, "请输入价格", Toast.LENGTH_SHORT).show();
                    return;
                }

                String desc = et_desc.getText().toString();
                if (TextUtils.isEmpty(desc)) {
                    Toast.makeText(this, "请输入描述", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(mBook == null){
                    mBook = new Book();
                }
                mBook.setName(name);
                mBook.setPrice(Integer.valueOf(price));
                mBook.setDesc(desc);

                Intent intent = new Intent();
                intent.putExtra("book", mBook);
                intent.putExtra("position",mPosition);
                setResult(200, intent);
                finish();

                break;
            case R.id.tv_del:

                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否删除该书籍？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra("position", mPosition);
                                setResult(201, intent);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


                break;
        }
    }
}

