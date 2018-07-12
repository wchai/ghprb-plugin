package org.jenkinsci.plugins.ghprb_lp.jobdsl;

import javaposse.jobdsl.dsl.Context;

class GhprbCancelBuildsOnUpdateContext implements Context {
    private Boolean overrideGlobal;

    public Boolean getOverrideGlobal() {
        return overrideGlobal;
    }

    /**
     * sets the overrideGlobal value
     */
    public void overrideGlobal(Boolean overrideGlobal) {
        this.overrideGlobal = overrideGlobal;
    }
}
