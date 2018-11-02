package com.baosong.supplyme.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Supply Me.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final Storage storage = new Storage();

    public Storage getStorage() {
        return this.storage;
    }

    /**
     * Storage configuration
     */
    public static class Storage {
        private String rootPath;

        private Attachments attachments;

        public String getRootPath() {
            return this.rootPath;
        }

        public void setRootPath(String rootPath) {
            this.rootPath = rootPath;
        }

        /**
         * @return the attachments configuration
         */
        public Attachments getAttachments() {
            return attachments;
        }

        /**
         * @param attachments the attachments to set
         */
        public void setAttachments(Attachments attachments) {
            this.attachments = attachments;
        }

        /**
         * Attachments storage configuration
         */
        public static class Attachments {

            private String path;

            private Long retentionDuration = 3600L;

            private String temporaryPath;

            /**
             * @return the path
             */
            public String getPath() {
                return path;
            }

            /**
             * @param path the path to set
             */
            public void setPath(String path) {
                this.path = path;
            }

            public Long getRetentionDuration() {
                return this.retentionDuration;
            }

            public void setRetentionDuration(Long retentionDuration) {
                this.retentionDuration = retentionDuration;
            }

            /**
             * @return the temporaryPath
             */
            public String getTemporaryPath() {
                return temporaryPath;
            }

            /**
             * @param temporaryPath the temporaryPath to set
             */
            public void setTemporaryPath(String temporaryPath) {
                this.temporaryPath = temporaryPath;
            }
        }
    }
}
