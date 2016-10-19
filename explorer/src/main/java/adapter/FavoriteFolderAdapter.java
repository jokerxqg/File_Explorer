package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joker.explorer.R;

import java.util.List;

import bean.FavoriteFolder;
import database.FavoriteHelper;

/**
 * Created by joker on 2016-10-19.
 */

public class FavoriteFolderAdapter extends BaseAdapter {

    private List<FavoriteFolder> list;
    private LayoutInflater layoutInflater;
    private FavoriteHelper helper;

    public FavoriteFolderAdapter(Context context, List<FavoriteFolder> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.helper = new FavoriteHelper(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_favorite_folder_item, null);
            viewHolder.tv_folderName = (TextView) convertView.findViewById(R.id.tv_folderName);
            viewHolder.tv_remove = (TextView) convertView.findViewById(R.id.tv_remove);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FavoriteFolder favoriteFolder = list.get(position);
        viewHolder.tv_folderName.setText(favoriteFolder.getFolderName());
        viewHolder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.delete(favoriteFolder.getFolderPath());
                deleteItem(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_folderName;
        TextView tv_remove;
    }

    public void deleteItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

}
