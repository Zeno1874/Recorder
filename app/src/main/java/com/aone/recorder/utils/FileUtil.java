package com.aone.recorder.utils;

import java.io.File;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.utils
 * @ClassName: FileUtil
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/14 3:51
 * @Desc:
 */
public class FileUtil {
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        file.delete();
//        if (file.isFile() && file.exists()) {
//            return file.delete();
//        }
    }
}
