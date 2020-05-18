package cc.catgasm.HTLWSlidingPuzzle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdapterSub extends BaseAdapter {
    private Context context;
    private ArrayList<ImageCell> cells;


    public AdapterSub(Context context, ArrayList<ImageCell> cells) {
        this.context = context;
        this.cells = cells;
    }

    @Override
    public int getCount() {
        return cells.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("showing cell nr: " + position);
        ImageView view = new ImageView(context);
        view.setImageBitmap(cells.get(position).getBitmap());

        view.setMaxHeight(30);
        view.setMaxWidth(30);

        return view;
    }
}
