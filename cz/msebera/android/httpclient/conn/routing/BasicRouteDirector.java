package cz.msebera.android.httpclient.conn.routing;

import cz.msebera.android.httpclient.util.Args;

public final class BasicRouteDirector implements HttpRouteDirector {
    public final int nextStep(RouteInfo plan, RouteInfo fact) {
        Args.notNull(plan, "Planned route");
        if (fact == null || fact.getHopCount() <= 0) {
            if (plan.getHopCount() > 1) {
                return 2;
            }
            return 1;
        } else if (plan.getHopCount() > 1) {
            if (fact.getHopCount() <= 1) {
                step = -1;
            } else if (plan.getTargetHost().equals(fact.getTargetHost())) {
                int hopCount = plan.getHopCount();
                int hopCount2 = fact.getHopCount();
                if (hopCount < hopCount2) {
                    step = -1;
                } else {
                    for (int i = 0; i < hopCount2 - 1; i++) {
                        if (!plan.getHopTarget(i).equals(fact.getHopTarget(i))) {
                            step = -1;
                            break;
                        }
                    }
                    step = hopCount > hopCount2 ? 4 : ((!fact.isTunnelled() || plan.isTunnelled()) && (!fact.isLayered() || plan.isLayered())) ? (!plan.isTunnelled() || fact.isTunnelled()) ? (!plan.isLayered() || fact.isLayered()) ? plan.isSecure() != fact.isSecure() ? -1 : 0 : 5 : 3 : -1;
                }
            } else {
                step = -1;
            }
            return step;
        } else {
            step = fact.getHopCount() > 1 ? -1 : !plan.getTargetHost().equals(fact.getTargetHost()) ? -1 : plan.isSecure() != fact.isSecure() ? -1 : (plan.getLocalAddress() == null || plan.getLocalAddress().equals(fact.getLocalAddress())) ? 0 : -1;
            return step;
        }
    }
}
