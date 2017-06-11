package cn.xdeveloper.book.store;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xdeveloper.model.Book;

/**
 * Created by lai_book on 2017/6/10.
 */

public class BookAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {

    public BookAdapter(List<Book> data) {
        super(R.layout.item_list_book, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Book item) {
        helper.setText(R.id.tv_name,item.getName())
                .setText(R.id.tv_desc,item.getDesc())
                .setText(R.id.tv_price,item.getPrice()+"元");
    }
}
