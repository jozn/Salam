package com.path.android.jobqueue;

import android.content.Context;

public interface QueueFactory {
    JobQueue createNonPersistent$7a7ba21a$78fdc9e4(Long l, String str);

    JobQueue createPersistentQueue(Context context, Long l, String str, boolean z);
}
