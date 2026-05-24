CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    first_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    PRIMARY KEY (id)
);

ALTER TABLE user ADD CONSTRAINT uk_user_email UNIQUE (email);

CREATE TABLE admin (
    active BIT NOT NULL DEFAULT 0,
    gender VARCHAR(255),
    birthdate DATE,
    address VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255),
    phone VARCHAR(255),
    image LONGTEXT,
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES user(id)
);

CREATE TABLE candidate (
    cv VARCHAR(255),
    description VARCHAR(255),
    address VARCHAR(255),
    title VARCHAR(255),
    image VARCHAR(255),
    phone VARCHAR(255),
    date_of_birth DATETIME,
    gender VARCHAR(255),
    active BIT NOT NULL DEFAULT 0,
    cv_path VARCHAR(255),
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES user(id)
);

CREATE TABLE recruiter (
    address VARCHAR(255),
    creation_date DATETIME,
    image VARCHAR(255),
    active BIT NOT NULL DEFAULT 0,
    company_name VARCHAR(255),
    phone VARCHAR(255),
    website VARCHAR(255),
    description VARCHAR(255),
    id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES user(id)
);

CREATE TABLE offer (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255),
    description VARCHAR(255),
    type VARCHAR(255),
    address VARCHAR(255),
    salary DOUBLE,
    experience VARCHAR(255),
    publication_date DATE,
    expiration_date DATE,
    recruiter_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (recruiter_id) REFERENCES recruiter(id)
);

CREATE TABLE candidacy (
    id BIGINT NOT NULL AUTO_INCREMENT,
    submission_date DATETIME,
    status VARCHAR(255),
    score DOUBLE,
    candidate_id BIGINT,
    offer_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (candidate_id) REFERENCES candidate(id),
    FOREIGN KEY (offer_id) REFERENCES offer(id)
);

CREATE TABLE skill (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255),
    percentage VARCHAR(255),
    candidate_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

CREATE TABLE experience (
    id BIGINT NOT NULL AUTO_INCREMENT,
    company_name VARCHAR(255),
    job_title VARCHAR(255),
    start_exp_date DATE,
    end_exp_date DATE,
    description VARCHAR(255),
    candidate_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

CREATE TABLE education (
    id BIGINT NOT NULL AUTO_INCREMENT,
    diploma VARCHAR(255),
    university VARCHAR(255),
    end_date VARCHAR(255),
    description VARCHAR(255),
    candidate_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

CREATE TABLE contact (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    subject VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    message VARCHAR(255),
    send_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (send_id) REFERENCES user(id)
);

CREATE TABLE saved_jobs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    offer_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (offer_id) REFERENCES offer(id)
);

CREATE TABLE message (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(255),
    subject VARCHAR(255),
    message VARCHAR(255),
    send_id BIGINT,
    receive_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (send_id) REFERENCES user(id),
    FOREIGN KEY (receive_id) REFERENCES user(id)
);

CREATE TABLE question (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255),
    choice1 VARCHAR(255),
    choice2 VARCHAR(255),
    choice3 VARCHAR(255),
    response VARCHAR(255),
    offer_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (offer_id) REFERENCES offer(id)
);

CREATE TABLE blacklisted_token (
    token VARCHAR(255) NOT NULL,
    blacklisted_at DATETIME,
    expires_at DATETIME,
    PRIMARY KEY (token)
);
