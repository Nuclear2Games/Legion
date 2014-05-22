ALTER PROC new_comment_answer (
	@userId BIGINT,
	@commentId BIGINT,
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
		comments
	WHERE
			id = @commentId
	
	IF @points IS NOT NULL AND @id IS NOT NULL
	BEGIN
		INSERT INTO comment_answers (
			commentId,
			userId,
			date,
			content,
			likes,
			dislikes
		) VALUES (
			@commentId,
			@userId,
			GETDATE(),
			@content,
			0,
			0
		)
		
		UPDATE comments SET
			answers = answers + 1
		WHERE
				id = @commentId
		
		SELECT @@IDENTITY AS 'id'
	END
END
GO
