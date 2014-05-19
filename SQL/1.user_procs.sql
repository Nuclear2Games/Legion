ALTER PROC new_user (
	@login VARCHAR(255),
	@password VARCHAR(255)
) AS
BEGIN
	DECLARE @id AS BIGINT
	SELECT
		@id = id
	FROM
		users
	WHERE
			login = @login
	
	IF @id IS NULL
	BEGIN
		INSERT INTO users (
			login,
			password,
			points
		) VALUES (
			@login,
			@password,
			0
		)
		
		SELECT @@IDENTITY AS 'id'
	END
END
GO

ALTER PROC update_user (
	@id BIGINT,
	@name VARCHAR(255),
	@description varchar(MAX)
) AS
BEGIN
	UPDATE users SET
		name = @name,
		description = @description
	WHERE
			id = @id
END
GO

ALTER PROC update_user_password (
	@id BIGINT,
	@passwordOld VARCHAR(255),
	@passwordNew VARCHAR(255)
) AS
BEGIN
	UPDATE users SET
		password = @passwordNew
	WHERE
			id = @id
		AND password = @passwordOld
END
GO

ALTER PROC login_user (
	@login VARCHAR(255),
	@password VARCHAR(255)
) AS
BEGIN
	SELECT
		id
	FROM
		users
	WHERE
			login = @login
		AND password = @password
END
GO

