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
	customId BIGINT,
	customTypeId TINYINT,
	date datetime,

	PRIMARY KEY (id)
);
CREATE INDEX checkins_user ON likes(userId);
CREATE INDEX checkins_custom ON likes(customId);

CREATE TABLE checkins (
	userId BIGINT,
	placeId BIGINT,
	date datetime,

	PRIMARY KEY (userId, placeId)
);
CREATE INDEX checkins_user ON checkins(userId);
CREATE INDEX checkins_place ON checkins(placeId);

CREATE TABLE places (
	id BIGINT IDENTITY,
	userId BIGINT,
	latitude FLOAT,
	longitude FLOAT,
	type BIGINT,
	name VARCHAR(255),
	description VARCHAR(MAX),
	date DATETIME,
	points BIGINT,
	checkins BIGINT,
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
CREATE INDEX subjects_user ON subjects(userId);

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
CREATE INDEX comment_user ON comments(userId);

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
CREATE INDEX comment_answer_user ON comment_answers(userId);
