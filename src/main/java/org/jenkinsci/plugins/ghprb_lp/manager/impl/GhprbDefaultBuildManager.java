package org.jenkinsci.plugins.ghprb_lp.manager.impl;

import hudson.model.Run;

/**
 * @author mdelapenya (Manuel de la Peña)
 */
public class GhprbDefaultBuildManager extends GhprbBaseBuildManager {

    public GhprbDefaultBuildManager(Run<?, ?> build) {
        super(build);
    }

}
