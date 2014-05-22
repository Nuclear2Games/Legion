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
		@id = id
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
			CAST(ROUND(SQRT(@points)) AS BIGINT),
			0,
			0,
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
