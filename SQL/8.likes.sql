ALTER PROC new_like (
	@userId BIGINT,
	@customId BIGINT,
	@customTypeId TINYINT,
	@isLike BIT
) AS
BEGIN
	DECLARE @id AS BIGINT
	DECLARE @already AS BIGINT
	DECLARE @lk AS INT
	DECLARE @dl AS INT

	SELECT
		@already = id
	FROM
		likes
	WHERE
			userId = @userId
		AND	customId = @customId
		AND	customTypeId = @customTypeId
	
	IF @already IS NULL
	BEGIN
		IF @isLike
		BEGIN
			SET @lk = 1
			SET @dl = 0
		END
		ELSE
		BEGIN
			SET @lk = 0
			SET @dl = 1
		END

		IF @customTypeId = 1 -- Places
		BEGIN
			SELECT
				@id = id
			FROM
				places
			WHERE
					id = @customId

			IF @id IS NOT NULL
			BEGIN
				UPDATE places SET
					likes = likes + @lk,
					dislikes = dislikes + @dl
				WHERE
						id = @id
			END
		END

		IF @customTypeId = 2 -- Subjects
		BEGIN
			SELECT
				@id = id
			FROM
				subjects
			WHERE
					id = @customId

			IF @id IS NOT NULL
			BEGIN
				UPDATE subjects SET
					likes = likes + @lk,
					dislikes = dislikes + @dl
				WHERE
						id = @id
			END
		END

		IF @customTypeId = 3 -- Comments
		BEGIN
			SELECT
				@id = id
			FROM
				comments
			WHERE
					id = @customId

			IF @id IS NOT NULL
			BEGIN
				UPDATE comments SET
					likes = likes + @lk,
					dislikes = dislikes + @dl
				WHERE
						id = @id
			END
		END

		IF @customTypeId = 4 -- Answers
		BEGIN
			SELECT
				@id = id
			FROM
				answers
			WHERE
					id = @customId

			IF @id IS NOT NULL
			BEGIN
				UPDATE answers SET
					likes = likes + @lk,
					dislikes = dislikes + @dl
				WHERE
						id = @id
			END
		END
		
		IF @id IS NOT NULL
		BEGIN
			INSERT INTO likes (
				userId,
				customId,
				customTypeId,
				date
			) VALUES (
				@userId,
				@customId,
				@customTypeId,
				GETDATE()
			)
			
			UPDATE users SET
				points = points + @lk - @dl * 3
			WHERE
					id = @userId
		END
	END
END
GO
