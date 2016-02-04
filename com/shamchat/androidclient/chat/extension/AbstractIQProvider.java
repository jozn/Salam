package com.shamchat.androidclient.chat.extension;

import org.jivesoftware.smack.provider.IQProvider;

public abstract class AbstractIQProvider<T extends IQ> extends AbstractProvider<T> implements IQProvider {
}
