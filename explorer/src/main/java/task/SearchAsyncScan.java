package task;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bean.SearchFile;
import fixed.FileType;
import utils.FileUtils;

/**
 * Created by joker on 2016-10-17.
 */

public class SearchAsyncScan extends AsyncTask<String, Integer, List<SearchFile>> {

    private String content;
    private List<SearchFile> searchFiles;

    @Override
    protected void onPreExecute() {
        searchFiles = new ArrayList<>();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<SearchFile> searchFiles) {
        super.onPostExecute(searchFiles);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected List<SearchFile> doInBackground(String... params) {
        content = params[0];
        List<SearchFile> searchFiles = getSearchList(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));

        return searchFiles;
    }


    private List<SearchFile> getSearchList(File f) {
        if (f.isFile()) {
            String name = f.getName();
            if (name.contains(content)) {
                if (!FileType.ERROR_FILE.equals(FileUtils.judgeFileType(name))) {
                    SearchFile searchFile = new SearchFile();
                    searchFile.setName(name);
                    searchFile.setFilePath(f.getPath());
                    searchFile.setFileType(FileUtils.judgeFileType(name));
                    searchFiles.add(searchFile);
                }
            }
        } else {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    getSearchList(file_str);
                }
            }
        }

        return searchFiles;
    }
}
