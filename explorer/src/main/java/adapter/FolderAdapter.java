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

import bean.Folder;
import fixed.FileType;
import utils.FileUtils;

/**
 * Created by joker on 2016/8/8.
 */
public class FolderAdapter extends BaseAdapter {

    private List<Folder> folderList;
    private LayoutInflater layoutInflater;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_folder_item, null);
            holder.tv_folderName = (TextView) convertView.findViewById(R.id.tv_folderName);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.iv_fileType = (ImageView) convertView.findViewById(R.id.iv_fileType);
            holder.tv_lastModified = (TextView) convertView.findViewById(R.id.tv_lastModified);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Folder folder = folderList.get(position);

        holder.tv_folderName.setText(folder.getFolderName());
        holder.tv_lastModified.setText(folder.getLastModified());
        holder.checkBox.setChecked(folder.isChecked());
        String fileType = folder.getFileType();
        int imageId = 0;

        if (folder.isDirectory()) {
            imageId = R.mipmap.ic_folder;
        } else {
            imageId = FileUtils.changeFileIcon(fileType);
        }

        holder.iv_fileType.setImageResource(imageId);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folder.setChecked(true);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_folderName;
        CheckBox checkBox;
        ImageView iv_fileType;
        TextView tv_lastModified;
    }
}
