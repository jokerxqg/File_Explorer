package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.joker.explorer.R;

import java.util.List;

import bean.Files;

/**
 * Created by joker on 2016-09-12.
 */
public class FileAdapter extends BaseAdapter {

    private List<Files> fileList;
    private LayoutInflater layoutInflater;

    public FileAdapter(List<Files> fileList, Context context) {
        this.fileList = fileList;
        layoutInflater = layoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_file_list_item, null);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.iv_fileIcon = (ImageView) convertView.findViewById(R.id.iv_fileIcon);
            holder.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
            holder.tv_fileSize = (TextView) convertView.findViewById(R.id.tv_fileSize);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Files file = fileList.get(position);
        holder.iv_fileIcon.setImageResource(file.getIcon());
        holder.checkBox.setChecked(file.isChecked());
        holder.tv_fileName.setText(file.getFileName());
        holder.tv_fileSize.setText(file.getFileSize());

        return convertView;
    }

    class ViewHolder {
        CheckBox checkBox;
        ImageView iv_fileIcon;
        TextView tv_fileName;
        TextView tv_fileSize;
    }
}
