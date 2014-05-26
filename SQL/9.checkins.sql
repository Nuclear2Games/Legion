ALTER PROC new_checkin (
	@userId BIGINT,
	@placeId BIGINT
) AS
BEGIN
	DECLARE @already AS BIGINT
	
	SELECT
		@already = userId
	FROM
		checkins
	WHERE
			userId = @userId
		AND	placeId = @placeId
	
	IF @already IS NULL
	BEGIN
		INSERT INTO checkins (
			userId,
			placeId,
			date
		) VALUES (
			@userId,
			@placeId,
 			GETDATE()
		)

		UPDATE places SET
			checkins = ISNULL(checkins, 0) + 1
		WHERE
				id = @placeId
	END
END
GO
