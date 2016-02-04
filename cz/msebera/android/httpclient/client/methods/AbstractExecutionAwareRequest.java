package cz.msebera.android.httpclient.client.methods;

import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.client.utils.CloneUtils;
import cz.msebera.android.httpclient.concurrent.Cancellable;
import cz.msebera.android.httpclient.conn.ClientConnectionRequest;
import cz.msebera.android.httpclient.conn.ConnectionReleaseTrigger;
import cz.msebera.android.httpclient.message.AbstractHttpMessage;
import cz.msebera.android.httpclient.message.HeaderGroup;
import cz.msebera.android.httpclient.params.HttpParams;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractExecutionAwareRequest extends AbstractHttpMessage implements HttpRequest, AbortableHttpRequest, Cloneable {
    private final AtomicBoolean aborted;
    private final AtomicReference<Cancellable> cancellableRef;

    /* renamed from: cz.msebera.android.httpclient.client.methods.AbstractExecutionAwareRequest.1 */
    class C12411 implements Cancellable {
        final /* synthetic */ ClientConnectionRequest val$connRequest;

        C12411(ClientConnectionRequest clientConnectionRequest) {
            this.val$connRequest = clientConnectionRequest;
        }
    }

    /* renamed from: cz.msebera.android.httpclient.client.methods.AbstractExecutionAwareRequest.2 */
    class C12422 implements Cancellable {
        final /* synthetic */ ConnectionReleaseTrigger val$releaseTrigger;

        C12422(ConnectionReleaseTrigger connectionReleaseTrigger) {
            this.val$releaseTrigger = connectionReleaseTrigger;
        }
    }

    protected AbstractExecutionAwareRequest() {
        super((byte) 0);
        this.aborted = new AtomicBoolean(false);
        this.cancellableRef = new AtomicReference(null);
    }

    @Deprecated
    public final void setConnectionRequest(ClientConnectionRequest connRequest) {
        setCancellable(new C12411(connRequest));
    }

    @Deprecated
    public final void setReleaseTrigger(ConnectionReleaseTrigger releaseTrigger) {
        setCancellable(new C12422(releaseTrigger));
    }

    public final boolean isAborted() {
        return this.aborted.get();
    }

    private void setCancellable(Cancellable cancellable) {
        if (!this.aborted.get()) {
            this.cancellableRef.set(cancellable);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        AbstractExecutionAwareRequest clone = (AbstractExecutionAwareRequest) super.clone();
        clone.headergroup = (HeaderGroup) CloneUtils.cloneObject(this.headergroup);
        clone.params = (HttpParams) CloneUtils.cloneObject(this.params);
        return clone;
    }
}
