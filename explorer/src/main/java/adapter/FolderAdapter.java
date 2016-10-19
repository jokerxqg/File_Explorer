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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Folder;
import fixed.FileType;
import utils.FileUtils;

/**
 * Created by joker on 2016/8/8.
 */
public class FolderAdapter extends BaseAdapter {

    private List<Folder> folderList;
    private LayoutInflater layoutInflater;

    public List<Folder> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<Folder> folderList) {
        this.folderList = folderList;
    }

    public FolderAdapter(List<Folder> folderList, Context context) {
        this.folderList = folderList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.layout_folder_item, null);
            holder.tv_folderName = (TextView) convertView.findViewById(R.id.tv_folderName);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.iv_fileType = (ImageView) convertView.findViewById(R.id.iv_fileType);
            holder.tv_lastModified = (TextView) convertView.findViewById(R.id.tv_lastModified);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Folder folder = folderList.get(position);
        String name = folder.getFolderName();
        if (name.length() > 30) {
            String endName = name.substring(name.length() - 8, name.length());
            name = name.substring(0, 18) + "..." + endName;
        }
        holder.tv_folderName.setText(name);
        holder.tv_lastModified.setText(folder.getLastModified());
        holder.checkBox.setChecked(folder.isChecked());
        String fileType = folder.getFileType();
        int imageId = 0;

        if (folder.isDirectory()) {
            imageId = R.mipmap.ic_folder;
            File[] files = new File(folder.getFolderPath()).listFiles();
            int childItem = 0;
            if (FileUtils.hindSystemFile) {
                for (File file : files) {
                    if (!file.getName().subSequence(0, 1).equals(".")) {
                        childItem = childItem + 1;
                    }
                }
            } else {
                for (File file : files) {
                    childItem += 1;
                }
            }

            if (files.length > 0) {
                holder.tv_count.setText(childItem + " 项目");
            } else {
                holder.tv_count.setText("0 项目");
            }

        } else if (folder.isFile()) {
            holder.tv_count.setText(folder.getFileSize());
            imageId = FileUtils.changeFileIcon(fileType);
        } else {
            imageId = R.mipmap.filetype_error;
        }
        holder.iv_fileType.setImageResource(imageId);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    folderList.get(position).setChecked(true);
                } else {
                    folderList.get(position).setChecked(false);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_folderName;
        CheckBox checkBox;
        ImageView iv_fileType;
        TextView tv_lastModified;
        TextView tv_count;
    }
}
