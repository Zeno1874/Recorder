package com.aone.recorder.model;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.model
 * @ClassName: RecordFile
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 3:54
 * @Desc:
 */
public class RecordFile {
    private String FileName;
    private String FileFormat;
    private String FilePath;
    private String FileRecordLength;
    private String FileCreatedTime;

    public RecordFile() {
    }

    public RecordFile(String FileName, String FileFormat, String FilePath, String FileRecordLength, String FileCreatedTime) {
        this.FileName = FileName;
        this.FileFormat = FileFormat;
        this.FilePath = FilePath;
        this.FileRecordLength = FileRecordLength;
        this.FileCreatedTime = FileCreatedTime;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileFormat() {
        return FileFormat;
    }

    public void setFileFormat(String fileFormat) {
        FileFormat = fileFormat;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFileRecordLength() {
        return FileRecordLength;
    }

    public void setFileRecordLength(String fileRecordLength) {
        FileRecordLength = fileRecordLength;
    }

    public String getFileCreatedTime() {
        return FileCreatedTime;
    }

    public void setFileCreatedTime(String fileCreatedTime) {
        FileCreatedTime = fileCreatedTime;
    }
}
