package com.peekaboo.transformation;

public abstract class FFMPEGLocator {
    public FFMPEGLocator() {
    }

    protected abstract String getFFMPEGExecutablePath();

    FFMPEGExecutor createExecutor() {
        return new FFMPEGExecutor(this.getFFMPEGExecutablePath());
    }
}
