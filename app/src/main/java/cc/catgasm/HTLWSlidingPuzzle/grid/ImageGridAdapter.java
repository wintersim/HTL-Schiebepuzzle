package cc.catgasm.HTLWSlidingPuzzle.grid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import cc.catgasm.HTLWSlidingPuzzle.R;

public class ImageGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ImageCell> cells;


    public ImageGridAdapter(Context context, ArrayList<ImageCell> cells) {
        this.context = context;
        this.cells = cells;
    }

    @Override
    public int getCount() {
        return cells.size();
    }

    @Override
    public Object getItem(int position) {
        return cells.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cells.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageViewHolder imageViewHolder = null;
        if(convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.image_cell, null);
            imageViewHolder = new ImageViewHolder();
            imageViewHolder.image = convertView.findViewById(R.id.grid_image_view);
            convertView.setTag(imageViewHolder);
        } else {
            imageViewHolder = (ImageViewHolder) convertView.getTag();
        }

        imageViewHolder.image.setImageBitmap(cells.get(position).getBitmap());

        return convertView;
    }

    public void setCells(ArrayList<ImageCell> cells) {
        this.cells = cells;
    }

    private static class ImageViewHolder {
        private ImageView image;
    }
}