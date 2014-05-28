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
		@id = userId
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
			@placeId,
			1,
			3,
			GETDATE(),
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
