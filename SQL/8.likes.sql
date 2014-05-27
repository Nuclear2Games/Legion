ALTER PROC new_like (
	@userId BIGINT,
	@customId BIGINT,
	@customTypeId TINYINT,
	@isLike BIT
) AS
BEGIN
	DECLARE @targetUserId AS BIGINT
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
		IF @isLike = 1
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
				@targetUserId = userId
			FROM
				places
			WHERE
					id = @customId

			IF @targetUserId IS NOT NULL
			BEGIN
				UPDATE places SET
					likes = ISNULL(likes, 0) + @lk,
					dislikes = ISNULL(dislikes, 0) + @dl
				WHERE
						id = @customId
			END
		END

		IF @customTypeId = 2 -- Subjects
		BEGIN
			SELECT
				@targetUserId = userId
			FROM
				subjects
			WHERE
					id = @customId

			IF @targetUserId IS NOT NULL
			BEGIN
				UPDATE subjects SET
					likes = ISNULL(likes, 0) + @lk,
					dislikes = ISNULL(dislikes, 0) + @dl
				WHERE
						id = @customId
			END
		END

		IF @customTypeId = 3 -- Comments
		BEGIN
			SELECT
				@targetUserId = userId
			FROM
				comments
			WHERE
					id = @customId

			IF @targetUserId IS NOT NULL
			BEGIN
				UPDATE comments SET
					likes = ISNULL(likes, 0) + @lk,
					dislikes = ISNULL(dislikes, 0) + @dl
				WHERE
						id = @customId
			END
		END

		IF @customTypeId = 4 -- comment_answers
		BEGIN
			SELECT
				@targetUserId = userId
			FROM
				comment_answers
			WHERE
					id = @customId

			IF @targetUserId IS NOT NULL
			BEGIN
				UPDATE comment_answers SET
					likes = ISNULL(likes, 0) + @lk,
					dislikes = ISNULL(dislikes, 0) + @dl
				WHERE
						id = @customId
			END
		END
		
		IF @targetUserId IS NOT NULL
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
			
			INSERT INTO notifications (
				userId,
				customId,
				customTypeId,
				what,
				date,
				seen
			) VALUES (
				@targetUserId,
				@customId,
				@customTypeId,
				CASE WHEN @isLike = 1 THEN 1 ELSE 2 END,
				GETDATE(),
				0
			)
			
			UPDATE users SET
				points = ISNULL(points, 0) + @lk - @dl * 3
			WHERE
					id = @targetUserId
		END
	END
END
GO
