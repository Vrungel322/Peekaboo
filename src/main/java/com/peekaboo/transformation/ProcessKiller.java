package com.peekaboo.transformation;

/**
 * Created by Oleksii on 06.09.2016.
 */
class ProcessKiller extends Thread {
    private Process process;

    public ProcessKiller(Process process) {
        this.process = process;
    }

    public void run() {
        this.process.destroy();
    }
}
