package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.List;

class AccessibilityNodeProviderCompatKitKat {

    interface AccessibilityNodeInfoBridge {
        Object createAccessibilityNodeInfo(int i);

        List<Object> findAccessibilityNodeInfosByText(String str, int i);

        Object findFocus(int i);

        boolean performAction(int i, int i2, Bundle bundle);
    }

    /* renamed from: android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.1 */
    static class C01141 extends AccessibilityNodeProvider {
        final /* synthetic */ AccessibilityNodeInfoBridge val$bridge;

        C01141(AccessibilityNodeInfoBridge accessibilityNodeInfoBridge) {
            this.val$bridge = accessibilityNodeInfoBridge;
        }

        public final AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
            return (AccessibilityNodeInfo) this.val$bridge.createAccessibilityNodeInfo(virtualViewId);
        }

        public final List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
            return this.val$bridge.findAccessibilityNodeInfosByText(text, virtualViewId);
        }

        public final boolean performAction(int virtualViewId, int action, Bundle arguments) {
            return this.val$bridge.performAction(virtualViewId, action, arguments);
        }

        public final AccessibilityNodeInfo findFocus(int focus) {
            return (AccessibilityNodeInfo) this.val$bridge.findFocus(focus);
        }
    }

    AccessibilityNodeProviderCompatKitKat() {
    }

    public static Object newAccessibilityNodeProviderBridge(AccessibilityNodeInfoBridge bridge) {
        return new C01141(bridge);
    }
}
