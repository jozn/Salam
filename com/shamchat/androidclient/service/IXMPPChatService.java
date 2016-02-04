package com.shamchat.androidclient.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.appcompat.C0170R;
import com.kyleduo.switchbutton.C0473R;
import com.shamchat.androidclient.IXMPPChatCallback;
import com.squareup.okhttp.internal.http.HttpEngine;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public interface IXMPPChatService extends IInterface {

    public static abstract class Stub extends Binder implements IXMPPChatService {

        private static class Proxy implements IXMPPChatService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public final IBinder asBinder() {
                return this.mRemote;
            }

            public final int getConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final String getConnectionStateString() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void setStatusFromConfig() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void disconnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void connect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final String changePassword(String newPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(newPassword);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void addRosterItem(String user, String alias, String group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(user);
                    _data.writeString(alias);
                    _data.writeString(group);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void addRosterGroup(String group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(group);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void renameRosterGroup(String group, String newGroup) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(group);
                    _data.writeString(newGroup);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void removeRosterItem(String user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(user);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void sendPresenceRequest(String user, String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(user);
                    _data.writeString(type);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void renameRosterItem(String user, String newName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(user);
                    _data.writeString(newName);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void moveRosterItemToGroup(String user, String group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(user);
                    _data.writeString(group);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void registerChatCallback(IXMPPChatCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void unregisterChatCallback(IXMPPChatCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final boolean isAuthenticated() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final void clearNotifications(String jid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(jid);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final String sendMessage(String toJID, String message, String messageType, boolean isGroupChat, String packetId, String latitude, String longitude, String description) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(toJID);
                    _data.writeString(message);
                    _data.writeString(messageType);
                    if (isGroupChat) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(packetId);
                    _data.writeString(latitude);
                    _data.writeString(longitude);
                    _data.writeString(description);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final String createRoom(String roomName, String nickName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(roomName);
                    _data.writeString(nickName);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final String addFriendsToChatRoom(String chatRoom, List<String> friendsJIDs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(chatRoom);
                    _data.writeStringList(friendsJIDs);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final String sendSeenPacket(String packetId, String to, String threadId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(packetId);
                    _data.writeString(to);
                    _data.writeString(threadId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final long getLastActivity(String friendJabberId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(friendJabberId);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public final String onComposing(String toJabberId, String composingText, boolean isGroupChat) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.shamchat.androidclient.service.IXMPPChatService");
                    _data.writeString(toJabberId);
                    _data.writeString(composingText);
                    if (isGroupChat) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.shamchat.androidclient.service.IXMPPChatService");
        }

        public static IXMPPChatService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("com.shamchat.androidclient.service.IXMPPChatService");
            if (iin == null || !(iin instanceof IXMPPChatService)) {
                return new Proxy(obj);
            }
            return (IXMPPChatService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String _result;
            switch (code) {
                case Logger.SEVERE /*1*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    int _result2 = getConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case Logger.WARNING /*2*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    _result = getConnectionStateString();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case Logger.INFO /*3*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    setStatusFromConfig();
                    reply.writeNoException();
                    return true;
                case Logger.CONFIG /*4*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    disconnect();
                    reply.writeNoException();
                    return true;
                case Logger.FINE /*5*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    connect();
                    reply.writeNoException();
                    return true;
                case Logger.FINER /*6*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    _result = changePassword(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case Logger.FINEST /*7*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    addRosterItem(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    addRosterGroup(data.readString());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    renameRosterGroup(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    removeRosterItem(data.readString());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    sendPresenceRequest(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    renameRosterItem(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    moveRosterItemToGroup(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    registerChatCallback(com.shamchat.androidclient.IXMPPChatCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_radius /*15*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    unregisterChatCallback(com.shamchat.androidclient.IXMPPChatCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    boolean _result3 = isAuthenticated();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case C0473R.styleable.SwitchButton_insetLeft /*17*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    clearNotifications(data.readString());
                    reply.writeNoException();
                    return true;
                case C0473R.styleable.SwitchButton_insetRight /*18*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    _result = sendMessage(data.readString(), data.readString(), data.readString(), data.readInt() != 0, data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case C0473R.styleable.SwitchButton_insetTop /*19*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    _result = createRoom(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case HttpEngine.MAX_FOLLOW_UPS /*20*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    _result = addFriendsToChatRoom(data.readString(), data.createStringArrayList());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case C0170R.styleable.Toolbar_navigationContentDescription /*21*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    _result = sendSeenPacket(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case C0170R.styleable.Toolbar_logoDescription /*22*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    long _result4 = getLastActivity(data.readString());
                    reply.writeNoException();
                    reply.writeLong(_result4);
                    return true;
                case C0170R.styleable.Toolbar_titleTextColor /*23*/:
                    data.enforceInterface("com.shamchat.androidclient.service.IXMPPChatService");
                    _result = onComposing(data.readString(), data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 1598968902:
                    reply.writeString("com.shamchat.androidclient.service.IXMPPChatService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    String addFriendsToChatRoom(String str, List<String> list) throws RemoteException;

    void addRosterGroup(String str) throws RemoteException;

    void addRosterItem(String str, String str2, String str3) throws RemoteException;

    String changePassword(String str) throws RemoteException;

    void clearNotifications(String str) throws RemoteException;

    void connect() throws RemoteException;

    String createRoom(String str, String str2) throws RemoteException;

    void disconnect() throws RemoteException;

    int getConnectionState() throws RemoteException;

    String getConnectionStateString() throws RemoteException;

    long getLastActivity(String str) throws RemoteException;

    boolean isAuthenticated() throws RemoteException;

    void moveRosterItemToGroup(String str, String str2) throws RemoteException;

    String onComposing(String str, String str2, boolean z) throws RemoteException;

    void registerChatCallback(IXMPPChatCallback iXMPPChatCallback) throws RemoteException;

    void removeRosterItem(String str) throws RemoteException;

    void renameRosterGroup(String str, String str2) throws RemoteException;

    void renameRosterItem(String str, String str2) throws RemoteException;

    String sendMessage(String str, String str2, String str3, boolean z, String str4, String str5, String str6, String str7) throws RemoteException;

    void sendPresenceRequest(String str, String str2) throws RemoteException;

    String sendSeenPacket(String str, String str2, String str3) throws RemoteException;

    void setStatusFromConfig() throws RemoteException;

    void unregisterChatCallback(IXMPPChatCallback iXMPPChatCallback) throws RemoteException;
}
