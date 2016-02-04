package org.jivesoftware.smackx.commands;

import org.jivesoftware.smackx.commands.packet.AdHocCommandData;

public abstract class LocalCommand extends AdHocCommand {
    long creationDate;
    int currenStage;
    String sessionID;

    public abstract boolean hasPermission$552c4dfd();

    public abstract boolean isLastStage();

    public LocalCommand() {
        this.creationDate = System.currentTimeMillis();
        this.currenStage = -1;
    }

    final void setData(AdHocCommandData data) {
        data.setSessionID(this.sessionID);
        super.setData(data);
    }

    final void incrementStage() {
        this.currenStage++;
    }
}
