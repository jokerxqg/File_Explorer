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

    public void setFileList(List<Files> fileList) {
        this.fileList = fileList;
    }

    public List<Files> getFileList() {
        return fileList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.checkBox.setChecked(file.isChecked());
        String fileName = file.getFileName();
        if(fileName.length()>32){
            String endName = fileName.substring(fileName.length()-8,fileName.length());
            fileName = fileName.substring(0,22)+"..."+endName;
        }
        holder.tv_fileName.setText(fileName);
        holder.tv_fileSize.setText(file.getFileSize());
        holder.iv_fileIcon.setImageBitmap(file.getIcon());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    fileList.get(position).setChecked(true);
                } else {
                    fileList.get(position).setChecked(false);
                }
            }
        });

        return convertView;
    }


    class ViewHolder {
        CheckBox checkBox;
        ImageView iv_fileIcon;
        TextView tv_fileName;
        TextView tv_fileSize;
    }
}
