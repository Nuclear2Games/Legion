ALTER PROC new_place (
	@userId BIGINT,
	@latitude float,
	@longitude float,
	@type int,
	@name VARCHAR(255),
	@description varchar(MAX),
	@date DATETIME
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

	IF @points IS NOT NULL AND @points >= 0
	BEGIN
		INSERT INTO places (
			userId,
			latitude,
			longitude,
			type,
			name,
			description,
			date,
			points,
			subjects
		) VALUES (
			@userId,
			@latitude,
			@longitude,
			@type,
			@name,
			@description,
			@date,
			@points,
			0
		)
		
		SELECT @@IDENTITY AS 'id'
	END
END
GO

ALTER PROC update_place (
	@id BIGINT,
	@userId BIGINT,
	@latitude float,
	@longitude float,
	@name VARCHAR(255),
	@description varchar(MAX)
) AS
BEGIN
	UPDATE places SET
		latitude = @latitude,
		longitude = @longitude,
		name = @name,
		description = @description
	WHERE
			id = @id
		AND userId = @userId
END
GO

