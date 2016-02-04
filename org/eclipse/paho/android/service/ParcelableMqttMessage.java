package org.eclipse.paho.android.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class ParcelableMqttMessage extends MqttMessage implements Parcelable {
    public static final Creator<ParcelableMqttMessage> CREATOR;
    String messageId;

    /* renamed from: org.eclipse.paho.android.service.ParcelableMqttMessage.1 */
    static class C12721 implements Creator<ParcelableMqttMessage> {
        C12721() {
        }

        public final ParcelableMqttMessage createFromParcel(Parcel parcel) {
            return new ParcelableMqttMessage(parcel);
        }

        public final ParcelableMqttMessage[] newArray(int size) {
            return new ParcelableMqttMessage[size];
        }
    }

    ParcelableMqttMessage(MqttMessage original) {
        super(original.getPayload());
        this.messageId = null;
        setQos(original.getQos());
        setRetained(original.isRetained());
        setDuplicate(original.isDuplicate());
    }

    ParcelableMqttMessage(Parcel parcel) {
        super(parcel.createByteArray());
        this.messageId = null;
        setQos(parcel.readInt());
        boolean[] flags = parcel.createBooleanArray();
        setRetained(flags[0]);
        setDuplicate(flags[1]);
        this.messageId = parcel.readString();
    }

    public String getMessageId() {
        return this.messageId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeByteArray(getPayload());
        parcel.writeInt(getQos());
        parcel.writeBooleanArray(new boolean[]{isRetained(), isDuplicate()});
        parcel.writeString(this.messageId);
    }

    static {
        CREATOR = new C12721();
    }
}
