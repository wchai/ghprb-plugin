package org.jenkinsci.plugins.ghprb_lp.extensions;

import hudson.model.Run;
import hudson.model.TaskListener;

public interface GhprbCommentAppender {
    String postBuildComment(Run<?, ?> build, TaskListener listener);
}
