CREATE TABLE users (
	id BIGINT IDENTITY,
	login VARCHAR(255),
	password VARCHAR(255),
	name VARCHAR(255),
	description varchar(MAX),
	points BIGINT,
	
	PRIMARY KEY (id)
);
CREATE INDEX user_login ON users(login);

CREATE TABLE likes (
	id BIGINT IDENTITY,
	userId BIGINT,
	points BIGINT,
	customId BIGINT,
	customType VARCHAR(255),
	date datetime,

	PRIMARY KEY (id)
);

CREATE TABLE places (
	id BIGINT IDENTITY,
	userId BIGINT,
	latitude FLOAT,
	longitude FLOAT,
	type BIGINT,
	name VARCHAR(255),
	description varchar(MAX),
	date DATETIME,
	points BIGINT,
	subjects BIGINT,
	likes BIGINT,
	dislikes BIGINT,
	
	PRIMARY KEY (id)
);
CREATE INDEX place_user ON places(userId);
CREATE INDEX place_lat ON places(latitude);
CREATE INDEX place_lon ON places(longitude);
CREATE INDEX place_type ON places(type);

CREATE TABLE place_routes (
	id BIGINT IDENTITY,
	placeId BIGINT,
	latitude FLOAT,
	longitude FLOAT,
	orderId BIGINT,

	PRIMARY KEY (id)
);
CREATE INDEX place_route_place ON place_routes(placeId);

CREATE TABLE subjects (
	id BIGINT IDENTITY,
	placeId BIGINT,
	userId BIGINT,
	date DATETIME,
	content VARCHAR(255),
	points BIGINT,
	comments BIGINT,
	likes BIGINT,
	dislikes BIGINT,

	PRIMARY KEY (id)
);
CREATE INDEX subjects_place ON subjects(placeId);

CREATE TABLE comments (
	id BIGINT IDENTITY,
	subjectId BIGINT,
	userId BIGINT,
	date DATETIME,
	content VARCHAR(MAX),
	points BIGINT,
	answers BIGINT,
	likes BIGINT,
	dislikes BIGINT,

	PRIMARY KEY (id)
);
CREATE INDEX comment_subject ON comments(subjectId);

CREATE TABLE comment_answers (
	id BIGINT IDENTITY,
	commentId BIGINT,
	userId BIGINT,
	date DATETIME,
	content VARCHAR(MAX),
	likes BIGINT,
	dislikes BIGINT,

	PRIMARY KEY (id)
);
CREATE INDEX comment_answer_comment ON comment_answers(commentId);
