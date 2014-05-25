ALTER PROC get_user (
	@id BIGINT
) AS
BEGIN
	SELECT
		login,
		name,
		description,
		points
	FROM
		users
	WHERE
			id = @id
END
GO

ALTER PROC get_notifications (
	@userId BIGINT
) AS
BEGIN
	SELECT
		customId,
		customTypeId,
		what,
		MAX(date) as DATE,
		seen,
		COUNT(*) AS 'quantity'
	FROM
		notifications
	WHERE
			userId = @userId
	GROUP BY
		customId,
		customTypeId,
		what,
		seen
	ORDER BY
		date DESC
END
GO

ALTER PROC read_notifications (
	@userId BIGINT
) AS
BEGIN
	UPDATE notifications SET
		seen = 1
	WHERE
			userId = @userId
END
GO

ALTER PROC get_place (
	@id BIGINT
) AS
BEGIN
	SELECT
		p.id,
		p.latitude,
		p.longitude,
		p.type,
		p.name,
		p.description,
		p.date,
		p.points,
		p.subjects,
		p.likes,
		p.dislikes,
		u.id AS 'userId',
		ISNULL(u.name, u.login) AS 'userName'
	FROM
		places p INNER JOIN users u ON (
				p.userId = u.id
		)
	WHERE
			p.id = @id
END
GO

ALTER PROC get_near_places (
	@latitude float,
	@longitude float
) AS
BEGIN
	SELECT
		p.id,
		p.latitude,
		p.longitude,
		p.type,
		p.name,
		p.description,
		p.date,
		p.points,
		p.subjects,
		p.likes,
		p.dislikes,
		u.id AS 'userId',
		ISNULL(u.name, u.login) AS 'userName'
	FROM
		places p INNER JOIN users u ON (
				p.userId = u.id
		)
	WHERE
			p.latitude > @latitude - 0.8
		AND p.latitude < @latitude + 0.8
		AND p.longitude > @longitude - 0.8
		AND p.longitude < @longitude + 0.8
		AND (p.date IS NULL OR p.date > DATEADD(DAY, -1, GETDATE()))
	ORDER BY
		p.date,
		(p.points + p.subjects + p.likes - p.dislikes * 3) DESC
END
GO

ALTER PROC get_place_routes (
	@placeId BIGINT
) AS
BEGIN
	-- TODO
	SELECT
		*
	FROM
		place_routes
	WHERE
			placeId = @placeId
	ORDER BY
		orderId
END
GO

ALTER PROC get_subject (
	@id BIGINT
) AS
BEGIN
	SELECT
		s.id,
		s.placeId,
		s.date,
		s.content,
		s.points,
		s.comments,
		s.likes,
		s.dislikes,
		u.id AS 'userId',
		ISNULL(u.name, u.login) AS 'userName'
	FROM
		subjects s INNER JOIN users u ON (
				s.userId = u.id
		)
	WHERE
			s.id = @id
END
GO

ALTER PROC get_subjects (
	@placeId BIGINT
) AS
BEGIN
	SELECT TOP 100
		s.id,
		s.placeId,
		s.date,
		s.content,
		s.points,
		s.comments,
		s.likes,
		s.dislikes,
		u.id AS 'userId',
		ISNULL(u.name, u.login) AS 'userName'
	FROM
		subjects s INNER JOIN users u ON (
				s.userId = u.id
		)
	WHERE
			placeId = @placeId
	ORDER BY
		(s.points + s.comments + s.likes - s.dislikes * 3 - SQRT(DATEDIFF(MI, s.date, GETDATE()))) DESC
END
GO

ALTER PROC get_comment (
	@id BIGINT
) AS
BEGIN
	SELECT
		c.id,
		c.subjectId,
		c.date,
		c.content,
		c.points,
		c.answers,
		c.likes,
		c.dislikes,
		u.id AS 'userId',
		ISNULL(u.name, u.login) AS 'userName'
	FROM
		comments c INNER JOIN users u ON (
				c.userId = u.id
		)
	WHERE
			c.id = @id
END
GO

ALTER PROC get_comments (
	@subjectId BIGINT
) AS
BEGIN
	SELECT
		c.id,
		c.subjectId,
		c.date,
		c.content,
		c.points,
		c.answers,
		c.likes,
		c.dislikes,
		u.id AS 'userId',
		ISNULL(u.name, u.login) AS 'userName'
	FROM
		comments c INNER JOIN users u ON (
				c.userId = u.id
		)
	WHERE
			subjectId = @subjectId
	ORDER BY
		(c.points + c.answers + c.likes - c.dislikes * 3 - SQRT(DATEDIFF(MI, c.date, GETDATE()))) DESC
END
GO

ALTER PROC get_answer (
	@id BIGINT
) AS
BEGIN
	SELECT
		ca.id,
		ca.commentId,
		ca.date,
		ca.content,
		ca.likes,
		ca.dislikes,
		u.id AS 'userId',
		ISNULL(u.name, u.login) AS 'userName'
	FROM
		comment_answers ca INNER JOIN users u ON (
				ca.userId = u.id
		)
	WHERE
			ca.id = @id
END
GO

ALTER PROC get_answers (
	@commentId BIGINT
) AS
BEGIN
	SELECT
		ca.id,
		ca.commentId,
		ca.date,
		ca.content,
		ca.likes,
		ca.dislikes,
		u.id AS 'userId',
		ISNULL(u.name, u.login) AS 'userName'
	FROM
		comment_answers ca INNER JOIN users u ON (
				ca.userId = u.id
		)
	WHERE
			commentId = @commentId
	ORDER BY
		date DESC
END
GO
