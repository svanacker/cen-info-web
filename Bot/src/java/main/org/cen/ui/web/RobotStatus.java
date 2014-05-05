package org.cen.ui.web;

import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class RobotStatus extends MultiAction {

    public Event display(RequestContext context) {
        return success();
    }
}
