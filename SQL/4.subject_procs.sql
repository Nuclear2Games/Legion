ALTER PROC new_subject (
	@userId BIGINT,
	@placeId BIGINT,
	@content VARCHAR(255)
) AS
BEGIN
	-- Get point of creator user
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
		places
	WHERE
			id = @placeId
	
	IF @points IS NOT NULL AND @points >= 0 AND @id IS NOT NULL
	BEGIN
		INSERT INTO subjects (
			placeId,
			userId,
			date,
			content,
			points,
			comments,
			likes,
			dislikes
		) VALUES (
			@placeId,
			@userId,
			GETDATE(),
			@content,
			CAST(ROUND(SQRT(@points)) AS BIGINT),
			0,
			0,
			0
		)
		
		UPDATE places SET
			subjects = subjects + 1
		WHERE
				id = @placeId
		
		SELECT @@IDENTITY AS 'id'
	END
END
GO
