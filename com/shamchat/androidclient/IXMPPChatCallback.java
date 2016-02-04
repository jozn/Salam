package com.shamchat.androidclient;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface IXMPPChatCallback extends IInterface {

    public static abstract class Stub extends Binder implements IXMPPChatCallback {

        private static class Proxy implements IXMPPChatCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public final IBinder asBinder() {
                return this.mRemote;
            }

            public final void connectionStateChanged(int connectionstate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.IXMPPChatCallback");
                    _data.writeInt(connectionstate);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void roomCreated(boolean created) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.IXMPPChatCallback");
                    if (created) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void didJoinRoom(boolean joined) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.IXMPPChatCallback");
                    if (joined) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void onFriendComposing(String jabberId, boolean isTypng) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.IXMPPChatCallback");
                    _data.writeString(jabberId);
                    if (isTypng) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.shamchat.androidclient.IXMPPChatCallback");
        }

        public static IXMPPChatCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("com.shamchat.androidclient.IXMPPChatCallback");
            if (iin == null || !(iin instanceof IXMPPChatCallback)) {
                return new Proxy(obj);
            }
            return (IXMPPChatCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            switch (code) {
                case Logger.SEVERE /*1*/:
                    data.enforceInterface("com.shamchat.androidclient.IXMPPChatCallback");
                    connectionStateChanged(data.readInt());
                    reply.writeNoException();
                    return true;
                case Logger.WARNING /*2*/:
                    data.enforceInterface("com.shamchat.androidclient.IXMPPChatCallback");
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    roomCreated(_arg0);
                    reply.writeNoException();
                    return true;
                case Logger.INFO /*3*/:
                    data.enforceInterface("com.shamchat.androidclient.IXMPPChatCallback");
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    didJoinRoom(_arg0);
                    reply.writeNoException();
                    return true;
                case Logger.CONFIG /*4*/:
                    boolean _arg1;
                    data.enforceInterface("com.shamchat.androidclient.IXMPPChatCallback");
                    String _arg02 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    onFriendComposing(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.shamchat.androidclient.IXMPPChatCallback");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void connectionStateChanged(int i) throws RemoteException;

    void didJoinRoom(boolean z) throws RemoteException;

    void onFriendComposing(String str, boolean z) throws RemoteException;

    void roomCreated(boolean z) throws RemoteException;
}
