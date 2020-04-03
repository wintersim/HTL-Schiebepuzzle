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
        int id = 0;
        try{
            Field idField = R.drawable.class.getDeclaredField(cells.get(position).getName());
            id = idField.getInt(idField);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        System.out.println("showing cell nr: " + position + "\t path: " + cells.get(position).getPath());
        ImageView view = new ImageView(context);
        view.setImageResource(id);

        view.setMaxHeight(30);
        view.setMaxWidth(30);

        return view;
    }
}
