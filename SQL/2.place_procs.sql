ALTER PROC new_place (
	@userId BIGINT,
	@latitude FLOAT,
	@longitude FLOAT,
	@type INT,
	@name VARCHAR(255),
	@description varchar(MAX),
	@date DATETIME = NULL
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
			CAST(SQRT(@points) AS BIGINT),
			0
		)
		
		SELECT @@IDENTITY AS 'id'
	END
END
GO

ALTER PROC update_place (
	@id BIGINT,
	@latitude FLOAT,
	@longitude FLOAT,
	@name VARCHAR(255),
	@description varchar(MAX),
	@date DATETIME = NULL
) AS
BEGIN
	UPDATE places SET
		latitude = @latitude,
		longitude = @longitude,
		name = @name,
		description = @description,
		date = @date
	WHERE
			id = @id
END
GO
