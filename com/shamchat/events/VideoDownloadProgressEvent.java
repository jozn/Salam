package com.shamchat.events;

public final class VideoDownloadProgressEvent {
    public String downloadedFilePath;
    public boolean isDone;
    public String packetId;
    public int percentCompleted;

    public VideoDownloadProgressEvent(String packetId, int percentCompleted, String downloadedFilePath, boolean isDone) {
        this.isDone = false;
        this.packetId = packetId;
        this.percentCompleted = percentCompleted;
        this.downloadedFilePath = downloadedFilePath;
        this.isDone = isDone;
    }
}
