package sms.exceptions;

public class UploadException extends BaseException {
    private String fileName;
    private String fileType;

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, String fileName, String fileType) {
        super(message);
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadException(String message, String fileName, String fileType, Throwable cause) {
        super(message, cause);
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    @Override
    public void log() {
        super.log();
        if (fileName != null) {
            System.err.println("File: " + fileName + " (Type: " + fileType + ")");
        }
    }
}