CREATE SEQUENCE IF NOT EXISTS revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE audit_log_actor_roles
(
    audit_log_id UUID NOT NULL,
    actor_roles  VARCHAR(255)
);

CREATE TABLE audit_logs
(
    id                UUID         NOT NULL,
    event_id          UUID         NOT NULL,
    correlation_id    UUID,
    timestamp         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    source_service    VARCHAR(255) NOT NULL,
    actor_user_id     VARCHAR(255) NOT NULL,
    actor_first_name  VARCHAR(255),
    actor_last_name   VARCHAR(255),
    action            VARCHAR(255) NOT NULL,
    resource_type     VARCHAR(255) NOT NULL,
    resource_id       VARCHAR(255) NOT NULL,
    before_state_json JSONB,
    after_state_json  JSONB,
    outcome           VARCHAR(255),
    failure_reason    VARCHAR(255),
    ip_address        VARCHAR(255),
    envers_revision   BIGINT,
    CONSTRAINT pk_audit_logs PRIMARY KEY (id)
);

CREATE TABLE revchanges
(
    rev        BIGINT NOT NULL,
    entityname VARCHAR(255)
);

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

ALTER TABLE audit_logs
    ADD CONSTRAINT uc_audit_logs_eventid UNIQUE (event_id);

CREATE INDEX idx_audit_actor ON audit_logs (actor_user_id);

CREATE INDEX idx_audit_correlation ON audit_logs (correlation_id);

CREATE INDEX idx_audit_resource ON audit_logs (resource_type, resource_id);

CREATE INDEX idx_audit_timestamp ON audit_logs (timestamp);

ALTER TABLE audit_log_actor_roles
    ADD CONSTRAINT fk_audit_log_actor_roles_on_audit_log_entity FOREIGN KEY (audit_log_id) REFERENCES audit_logs (id);

ALTER TABLE revchanges
    ADD CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev);
CREATE SEQUENCE IF NOT EXISTS revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE audit_logs
(
    id                UUID                        NOT NULL,
    event_id          UUID                        NOT NULL,
    correlation_id    UUID,
    timestamp         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    source_service    VARCHAR(255)                NOT NULL,
    actor_user_id     VARCHAR(255)                NOT NULL,
    actor_first_name  VARCHAR(255),
    actor_last_name   VARCHAR(255),
    actor_role        VARCHAR(255),
    action            VARCHAR(255)                NOT NULL,
    resource_type     VARCHAR(255)                NOT NULL,
    resource_id       VARCHAR(255)                NOT NULL,
    before_state_json JSONB,
    after_state_json  JSONB,
    outcome           VARCHAR(255),
    failure_reason    VARCHAR(255),
    ip_address        VARCHAR(255),
    envers_revision   BIGINT,
    CONSTRAINT pk_audit_logs PRIMARY KEY (id)
);

CREATE TABLE revchanges
(
    rev        BIGINT NOT NULL,
    entityname VARCHAR(255)
);

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

ALTER TABLE audit_logs
    ADD CONSTRAINT uc_audit_logs_eventid UNIQUE (event_id);

CREATE INDEX idx_audit_actor ON audit_logs (actor_user_id);

CREATE INDEX idx_audit_correlation ON audit_logs (correlation_id);

CREATE INDEX idx_audit_resource ON audit_logs (resource_type, resource_id);

CREATE INDEX idx_audit_timestamp ON audit_logs (timestamp);

ALTER TABLE revchanges
    ADD CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev);