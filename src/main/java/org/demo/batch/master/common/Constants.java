package org.demo.batch.master.common;

public final class Constants {
    private Constants() {
    }

    public static class Master {
        private Master() {
        }

        public static final String JOB_NAME = "JOB_MASTER_REMOTE_PARTITION";
        public static final String STEP_NAME = "STEP_MASTER_REMOTE_PARTITION";
    }

    public static class Worker {
        private Worker() {
        }

        public static final String STEP_REMOTE_PARTITION_NAME = "STEP_PARTITION_SLAVE_REMOTE_PARTITION";
        public static final String STEP_SLAVE_NAME = "STEP_SLAVE_REMOTE_PARTITION";
    }
}
