package com.rokhgroup.mqtt;

import android.content.Context;
import com.rokhgroup.mqtt.Connection.ConnectionStatus;
import com.shamchat.events.NewGroupMessageSentFailedEvent;
import com.shamchat.events.NewGroupMessageSentSuccessEvent;
import de.greenrobot.event.EventBus;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ActionListener implements IMqttActionListener {
    private boolean DEBUG;
    private int action$621bd8f2;
    private String[] additionalArgs;
    private String clientHandle;
    private Context context;

    /* renamed from: com.rokhgroup.mqtt.ActionListener.1 */
    static /* synthetic */ class C06721 {
        static final /* synthetic */ int[] $SwitchMap$com$rokhgroup$mqtt$ActionListener$Action;

        static {
            $SwitchMap$com$rokhgroup$mqtt$ActionListener$Action = new int[Action.values$41597bec().length];
            try {
                $SwitchMap$com$rokhgroup$mqtt$ActionListener$Action[Action.CONNECT$621bd8f2 - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$ActionListener$Action[Action.DISCONNECT$621bd8f2 - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$ActionListener$Action[Action.SUBSCRIBE$621bd8f2 - 1] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$ActionListener$Action[Action.PUBLISH$621bd8f2 - 1] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum Action {
        ;

        public static int[] values$41597bec() {
            return (int[]) $VALUES$1fa1c873.clone();
        }

        static {
            CONNECT$621bd8f2 = 1;
            DISCONNECT$621bd8f2 = 2;
            SUBSCRIBE$621bd8f2 = 3;
            PUBLISH$621bd8f2 = 4;
            $VALUES$1fa1c873 = new int[]{CONNECT$621bd8f2, DISCONNECT$621bd8f2, SUBSCRIBE$621bd8f2, PUBLISH$621bd8f2};
        }
    }

    public ActionListener(Context context, int action, String clientHandle, String... additionalArgs) {
        this.DEBUG = false;
        this.context = context;
        this.action$621bd8f2 = action;
        this.clientHandle = clientHandle;
        this.additionalArgs = additionalArgs;
    }

    public final void onSuccess(IMqttToken asyncActionToken) {
        Connection connection;
        switch (C06721.$SwitchMap$com$rokhgroup$mqtt$ActionListener$Action[this.action$621bd8f2 - 1]) {
            case Logger.SEVERE /*1*/:
                connection = Connections.getInstance(this.context).getConnection(this.clientHandle);
                connection.changeConnectionStatus$19646d7f(ConnectionStatus.CONNECTED$6abac5f4);
                connection.addAction("Client Connected");
                if (this.DEBUG) {
                    Notify.toast$4475a3b4(this.context, "connected to group server");
                }
            case Logger.WARNING /*2*/:
                connection = Connections.getInstance(this.context).getConnection(this.clientHandle);
                connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                String string = this.context.getString(2131493417);
                connection.addAction(string);
                if (this.DEBUG) {
                    Notify.toast$4475a3b4(this.context, "actionListener: server " + string);
                }
            case Logger.INFO /*3*/:
                Connection connection2 = Connections.getInstance(this.context).getConnection(this.clientHandle);
                CharSequence string2 = this.context.getString(2131493422, (Object[]) this.additionalArgs);
                connection2.addAction(string2);
                if (this.DEBUG) {
                    Notify.toast$4475a3b4(this.context, string2);
                } else {
                    Notify.toast$4475a3b4(this.context, "success");
                }
            case Logger.CONFIG /*4*/:
                Connections.getInstance(this.context).getConnection(this.clientHandle).addAction(this.context.getString(2131493419, (Object[]) this.additionalArgs));
                EventBus.getDefault().postSticky(new NewGroupMessageSentSuccessEvent(this.additionalArgs[0]));
            default:
        }
    }

    public final void onFailure(IMqttToken token, Throwable exception) {
        Connection connection;
        switch (C06721.$SwitchMap$com$rokhgroup$mqtt$ActionListener$Action[this.action$621bd8f2 - 1]) {
            case Logger.SEVERE /*1*/:
                connection = Connections.getInstance(this.context).getConnection(this.clientHandle);
                connection.changeConnectionStatus$19646d7f(ConnectionStatus.ERROR$6abac5f4);
                connection.addAction("Client failed to connect");
            case Logger.WARNING /*2*/:
                connection = Connections.getInstance(this.context).getConnection(this.clientHandle);
                connection.changeConnectionStatus$19646d7f(ConnectionStatus.DISCONNECTED$6abac5f4);
                connection.addAction("Disconnect Failed - an error occured");
            case Logger.INFO /*3*/:
                Connection connection2 = Connections.getInstance(this.context).getConnection(this.clientHandle);
                CharSequence string = this.context.getString(2131493421, (Object[]) this.additionalArgs);
                connection2.addAction(string);
                Notify.toast$4475a3b4(this.context, string);
            case Logger.CONFIG /*4*/:
                Connections.getInstance(this.context).getConnection(this.clientHandle).addAction(this.context.getString(2131493418, (Object[]) this.additionalArgs));
                EventBus.getDefault().postSticky(new NewGroupMessageSentFailedEvent(this.additionalArgs[0]));
            default:
        }
    }
}
