ALTER PROC new_comment (
	@userId BIGINT,
	@subjectId BIGINT,
	@content VARCHAR(MAX)
) AS
BEGIN
	DECLARE @points AS BIGINT
	SELECT
		@points = points
	FROM
		users
	WHERE
			id = @userId

	DECLARE @id AS BIGINT
	SELECT
		@id = userId
	FROM
		subjects
	WHERE
			id = @subjectId

	IF @points IS NOT NULL AND @id IS NOT NULL
	BEGIN
		INSERT INTO comments (
			subjectId,
			userId,
			date,
			content,
			points,
			answers,
			likes,
			dislikes
		) VALUES (
			@subjectId,
			@userId,
			GETDATE(),
			@content,
			CAST(SQRT(@points) AS BIGINT),
			0,
			0,
			0
		)
		
		INSERT INTO notifications (
			userId,
			customId,
			customTypeId,
			what,
			date,
			seen
		) VALUES (
			@id,
			@subjectId,
			3,
			3,
			GETDATE(),
			0
		)
		
		UPDATE subjects SET
			comments = comments + 1
		WHERE
				id = @subjectId
		
		SELECT @@IDENTITY AS 'id'
	END
END
GO
