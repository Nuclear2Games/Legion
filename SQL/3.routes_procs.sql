ALTER PROC new_route (
	@placeId BIGINT,
	@userId BIGINT,
	@latitude float,
	@longitude float,
	@orderId int
) AS
BEGIN
	DECLARE @id AS BIGINT
	SELECT
		@id = id
	FROM
		places
	WHERE
			id = @placeId
		AND userId = @userId
	
	IF @id IS NOT NULL
	BEGIN
		INSERT INTO place_routes (
			placeId,
			latitude,
			longitude,
			orderId
		) VALUES (
			@placeId,
			@latitude,
			@longitude,
			@orderId
		)
	END
END
GO

ALTER PROC clear_route (
	@placeId BIGINT,
	@userId BIGINT
) AS
BEGIN
	DECLARE @id AS BIGINT
	SELECT
		@id = id
	FROM
		places
	WHERE
			id = @placeId
		AND userId = @userId
	
	IF @id IS NOT NULL
	BEGIN
		DELETE FROM
			place_routes
		WHERE
				placeId = @placeId
	END
END
GO

