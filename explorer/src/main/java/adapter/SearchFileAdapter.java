package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joker.explorer.R;

import java.util.List;

import bean.SearchFile;
import utils.FileUtils;

/**
 * Created by joker on 2016-10-17.
 */

public class SearchFileAdapter extends BaseAdapter {
    private List<SearchFile> list;
    private LayoutInflater layoutInflater;

    public SearchFileAdapter(Context context, List<SearchFile> list) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_search_file_titem, null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SearchFile searchFile = list.get(position);


        int imageId = FileUtils.changeFileIcon(searchFile.getFileType());


        viewHolder.tv_name.setText(searchFile.getName());
        viewHolder.iv_icon.setImageResource(imageId);

        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
    }
}
