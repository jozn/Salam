package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/* compiled from: FragmentManager */
final class FragmentManagerState implements Parcelable {
    public static final Creator<FragmentManagerState> CREATOR;
    FragmentState[] mActive;
    int[] mAdded;
    BackStackState[] mBackStack;

    /* renamed from: android.support.v4.app.FragmentManagerState.1 */
    static class FragmentManager implements Creator<FragmentManagerState> {
        FragmentManager() {
        }

        public final FragmentManagerState createFromParcel(Parcel in) {
            return new FragmentManagerState(in);
        }

        public final FragmentManagerState[] newArray(int size) {
            return new FragmentManagerState[size];
        }
    }

    public FragmentManagerState(Parcel in) {
        this.mActive = (FragmentState[]) in.createTypedArray(FragmentState.CREATOR);
        this.mAdded = in.createIntArray();
        this.mBackStack = (BackStackState[]) in.createTypedArray(BackStackState.CREATOR);
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.mActive, flags);
        dest.writeIntArray(this.mAdded);
        dest.writeTypedArray(this.mBackStack, flags);
    }

    static {
        CREATOR = new FragmentManager();
    }
}
