using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Data.SqlClient;
using System.Net.Mail;
using System.Net;
using System.IO;

namespace Legion
{
    public class Legion : ILegion
    {
        private const int ID_UNKNOWN_ERROR = 2;
        private const string DESCRIPTION_UNKNOWN_ERROR = "Unknown error";

        private const int ID_INVALID_CONTENT = 3;
        private const string DESCRIPTION_INVALID_CONTENT = "Invalid content";

        private const int ID_INVALID_USER_OR_PASSWORD = 4;
        private const string DESCRIPTION_INVALID_USER_OR_PASSWORD = "Invalid user or password";

        private const int ID_EXISTING_USER = 5;
        private const string DESCRIPTION_EXISTING_USER = "Existing user";

        private const int ID_LOW_RATING = 6;
        private const string DESCRIPTION_LOW_RATING = "User hasn't enougth rating to create";

        #region Users
        public ResultId NewUser(string login, string password)
        {
            ResultId r = new ResultId();

            // Validate if login is valid
            if (login == null || login.Length < 4 || login.Length > 255 || password == null || password.Length < 4 || password.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("new_user", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@login"].Value = login;
                command.Parameters["@password"].Value = password;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    // If there is result, the ID will be returned
                    if (reader.Read())
                    {
                        if (!reader.IsDBNull(0))
                            r.Content = Convert.ToInt64(reader.GetValue(0));
                        if (r.Content == -1)
                        {
                            r.Code = ID_EXISTING_USER;
                            r.Message = DESCRIPTION_EXISTING_USER;
                        }
                    }
                    else
                    {
                        r.Code = ID_UNKNOWN_ERROR;
                        r.Message = DESCRIPTION_UNKNOWN_ERROR;
                    }
                }

            }
            con.Close();

            return r;
        }

        public ResultUser Login(string login, string password)
        {
            ResultUser r = new ResultUser();

            // Validate if login is valid
            if (login == null || login.Length < 4 || login.Length > 255 || password == null || password.Length < 4 || password.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("login_user", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@login"].Value = login;
                command.Parameters["@password"].Value = password;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    // If there is result, the ID will be returned
                    if (reader.Read())
                    {
                        User u = new User();

                        if (!reader.IsDBNull(0))
                            u.Id = reader.GetInt64(0);
                        if (!reader.IsDBNull(1))
                            u.Login = reader.GetString(1);
                        if (!reader.IsDBNull(2))
                            u.Name = reader.GetString(2);
                        if (!reader.IsDBNull(3))
                            u.Description = reader.GetString(3);
                        if (!reader.IsDBNull(4))
                            u.Points = reader.GetInt64(4);

                        r.Content = u;
                    }
                    else
                    {
                        r.Code = ID_INVALID_USER_OR_PASSWORD;
                        r.Message = DESCRIPTION_INVALID_USER_OR_PASSWORD;
                    }
                }

            }
            con.Close();

            return r;
        }

        public Result UpdateUser(long id, string name, string description)
        {
            Result r = new Result();

            // Validate if login is valid
            if (id <= 0 || name == null || name.Length < 4 || name.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("update_user", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@id"].Value = id;
                command.Parameters["@name"].Value = name;
                command.Parameters["@description"].Value = description;

                if (command.ExecuteNonQuery() == 0)
                {
                    r.Code = ID_UNKNOWN_ERROR;
                    r.Message = DESCRIPTION_UNKNOWN_ERROR;
                }
            }
            con.Close();

            return r;
        }

        public Result UpdatePassword(long id, string passwordOld, string passwordNew)
        {
            Result r = new Result();

            // Validate if login is valid
            if (id <= 0 || passwordOld == null || passwordOld.Length < 4 || passwordOld.Length > 255 || passwordNew == null || passwordNew.Length < 4 || passwordNew.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("update_user_password", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@id"].Value = id;
                command.Parameters["@passwordOld"].Value = passwordOld;
                command.Parameters["@passwordNew"].Value = passwordNew;

                if (command.ExecuteNonQuery() == 0)
                {
                    r.Code = ID_INVALID_USER_OR_PASSWORD;
                    r.Message = DESCRIPTION_INVALID_USER_OR_PASSWORD;
                }
            }
            con.Close();

            return r;
        }
        #endregion

        #region Places
        public ResultId NewPlace(long user, double latitude, double longitude, long type, string name, string description, string date)
        {
            ResultId r = new ResultId();

            // Validate if login is valid
            if (user <= 0 || latitude == 0 || longitude == 0 || type <= 0 || name == null || name.Length < 4 || name.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("new_place", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@userId"].Value = user;
                command.Parameters["@latitude"].Value = latitude;
                command.Parameters["@longitude"].Value = longitude;
                command.Parameters["@type"].Value = type;
                command.Parameters["@name"].Value = name;
                command.Parameters["@description"].Value = description;
                command.Parameters["@date"].Value = date;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    // If there is result, the ID will be returned
                    if (reader.Read())
                    {
                        if (!reader.IsDBNull(0))
                            r.Content = Convert.ToInt64(reader.GetValue(0));
                    }
                    else
                    {
                        r.Code = ID_LOW_RATING;
                        r.Message = DESCRIPTION_LOW_RATING;
                    }
                }

            }
            con.Close();

            return r;
        }

        public Result UpdatePlace(long user, long place, double latitude, double longitude, string name, string description, string date)
        {
            Result r = new Result();

            // Validate if login is valid
            if (user <= 0 || place <= 0 || latitude == 0 || longitude == 0 || name == null || name.Length < 4 || name.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("update_place", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@id"].Value = place;
                command.Parameters["@latitude"].Value = latitude;
                command.Parameters["@longitude"].Value = longitude;
                command.Parameters["@name"].Value = name;
                command.Parameters["@description"].Value = description;
                command.Parameters["@date"].Value = date;

                if (command.ExecuteNonQuery() == 0)
                {
                    r.Code = ID_UNKNOWN_ERROR;
                    r.Message = DESCRIPTION_UNKNOWN_ERROR;
                }
            }
            con.Close();

            return r;
        }
        #endregion

        #region Subjects
        public ResultId NewSubject(long user, long place, string content)
        {
            ResultId r = new ResultId();

            // Validate if login is valid
            if (user <= 0 || place <= 0 || content == null || content.Length < 4 || content.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("new_subject", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@userId"].Value = user;
                command.Parameters["@placeId"].Value = place;
                command.Parameters["@content"].Value = content;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    // If there is result, the ID will be returned
                    if (reader.Read())
                    {
                        if (!reader.IsDBNull(0))
                            r.Content = Convert.ToInt64(reader.GetValue(0));
                    }
                    else
                    {
                        r.Code = ID_LOW_RATING;
                        r.Message = DESCRIPTION_LOW_RATING;
                    }
                }
            }
            con.Close();

            return r;
        }
        #endregion

        #region Comments
        public ResultId NewComment(long user, long subject, string content)
        {
            ResultId r = new ResultId();

            // Validate if login is valid
            if (user <= 0 || subject <= 0 || content == null || content.Length < 4 || content.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("new_comment", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@userId"].Value = user;
                command.Parameters["@subjectId"].Value = subject;
                command.Parameters["@content"].Value = content;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    // If there is result, the ID will be returned
                    if (reader.Read())
                    {
                        if (!reader.IsDBNull(0))
                            r.Content = Convert.ToInt64(reader.GetValue(0));
                    }
                    else
                    {
                        r.Code = ID_LOW_RATING;
                        r.Message = DESCRIPTION_LOW_RATING;
                    }
                }
            }
            con.Close();

            return r;
        }
        #endregion

        #region Answers
        public ResultId NewAnswer(long user, long comment, string content)
        {
            ResultId r = new ResultId();

            // Validate if login is valid
            if (user <= 0 || comment <= 0 || content == null || content.Length < 4 || content.Length > 255)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("new_comment_answer", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@userId"].Value = user;
                command.Parameters["@commentId"].Value = comment;
                command.Parameters["@content"].Value = content;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    // If there is result, the ID will be returned
                    if (reader.Read())
                    {
                        if (!reader.IsDBNull(0))
                            r.Content = Convert.ToInt64(reader.GetValue(0));
                    }
                    else
                    {
                        r.Code = ID_LOW_RATING;
                        r.Message = DESCRIPTION_LOW_RATING;
                    }
                }
            }
            con.Close();

            return r;
        }
        #endregion

        #region Likes
        public Result NewLike(long user, long customId, long customTypeId, bool isLike)
        {
            Result r = new Result();

            // Validate if login is valid
            if (user <= 0 || customId <= 0 || customTypeId <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("new_like", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@userId"].Value = user;
                command.Parameters["@customId"].Value = customId;
                command.Parameters["@customTypeId"].Value = customTypeId;
                command.Parameters["@isLike"].Value = isLike;

                command.ExecuteNonQuery();
            }
            con.Close();

            return r;
        }
        #endregion

        #region Checkins
        public Result NewCheckin(long user, long place)
        {
            Result r = new Result();

            // Validate if login is valid
            if (user <= 0 || place <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("new_checkin", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@userId"].Value = user;
                command.Parameters["@placeId"].Value = place;

                command.ExecuteNonQuery();
            }
            con.Close();

            return r;
        }
        #endregion

        #region Gets...
        public ResultUser GetUser(long user)
        {
            ResultUser r = new ResultUser();

            // Validate if login is valid
            if (user <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("get_user", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@id"].Value = user;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    // If there is result, the ID will be returned
                    if (reader.Read())
                    {
                        User u = new User();

                        u.Id = user;
                        if (!reader.IsDBNull(0))
                            u.Login = reader.GetString(0);
                        if (!reader.IsDBNull(1))
                            u.Name = reader.GetString(1);
                        if (!reader.IsDBNull(2))
                            u.Description = reader.GetString(2);
                        if (!reader.IsDBNull(3))
                            u.Points = reader.GetInt64(3);

                        r.Content = u;
                    }
                    else
                    {
                        r.Code = ID_UNKNOWN_ERROR;
                        r.Message = DESCRIPTION_UNKNOWN_ERROR;
                    }
                }
            }
            con.Close();

            return r;
        }

        public ResultNotifications GetNotifications(long user)
        {
            ResultNotifications r = new ResultNotifications();

            // Validate if login is valid
            if (user <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            try
            {
                SqlConnection con;
                using (SqlCommand command = new SqlCommand("get_notifications", con = getConnection()))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    SqlCommandBuilder.DeriveParameters(command);

                    command.Parameters["@userId"].Value = user;

                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        List<Notification> notifications = new List<Notification>();

                        while (reader.Read())
                        {
                            Notification n = new Notification();

                            if (!reader.IsDBNull(0))
                                n.CustomId = reader.GetInt64(0);
                            if (!reader.IsDBNull(1))
                                n.CustomTypeId = reader.GetByte(1);
                            if (!reader.IsDBNull(2))
                                n.What = reader.GetInt64(2);
                            if (!reader.IsDBNull(3))
                                n.Date = reader.GetDateTime(3).ToString("dd/MM/yyyy HH:mm");
                            if (!reader.IsDBNull(4))
                                n.Seen = reader.GetBoolean(4);
                            if (!reader.IsDBNull(5))
                                n.Quantity = Convert.ToInt64(reader.GetValue(5));

                            notifications.Add(n);
                        }

                        Notification[] ns = new Notification[notifications.Count];

                        for (int i = 0; i < ns.Length; i++)
                            ns[i] = notifications[i];

                        r.Content = ns;
                    }
                }
                con.Close();
            }
            catch (Exception ex)
            {
                r.Code = ID_UNKNOWN_ERROR;
                r.Message = ex.Message;
            }

            return r;
        }

        public Result ReadNotifications(long user)
        {
            ResultNotifications r = new ResultNotifications();

            // Validate if login is valid
            if (user <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            try
            {
                SqlConnection con;
                using (SqlCommand command = new SqlCommand("read_notifications", con = getConnection()))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    SqlCommandBuilder.DeriveParameters(command);

                    command.Parameters["@userId"].Value = user;

                    command.ExecuteNonQuery();
                }
                con.Close();
            }
            catch (Exception ex)
            {
                r.Code = ID_UNKNOWN_ERROR;
                r.Message = ex.Message;
            }

            return r;
        }

        public ResultPlace GetPlace(long id)
        {
            ResultPlace r = new ResultPlace();

            // Validate if login is valid
            if (id <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            try
            {
                SqlConnection con;
                using (SqlCommand command = new SqlCommand("get_place", con = getConnection()))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    SqlCommandBuilder.DeriveParameters(command);

                    command.Parameters["@id"].Value = id;

                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            Place p = new Place();

                            if (!reader.IsDBNull(0))
                                p.Id = reader.GetInt64(0);
                            if (!reader.IsDBNull(1))
                                p.Latitude = reader.GetDouble(1);
                            if (!reader.IsDBNull(2))
                                p.Longitude = reader.GetDouble(2);
                            if (!reader.IsDBNull(3))
                                p.Type = reader.GetInt64(3);
                            if (!reader.IsDBNull(4))
                                p.Name = reader.GetString(4);
                            if (!reader.IsDBNull(5))
                                p.Description = reader.GetString(5);
                            if (!reader.IsDBNull(6))
                                p.Date = reader.GetDateTime(6).ToString("dd/MM/yyyy HH:mm");
                            if (!reader.IsDBNull(7))
                                p.Points = reader.GetInt64(7);
                            if (!reader.IsDBNull(8))
                                p.Checkins = reader.GetInt64(8);
                            if (!reader.IsDBNull(9))
                                p.Subjects = reader.GetInt64(9);
                            if (!reader.IsDBNull(10))
                                p.Likes = reader.GetInt64(10);
                            if (!reader.IsDBNull(11))
                                p.Dislikes = reader.GetInt64(11);
                            if (!reader.IsDBNull(12))
                                p.UserId = reader.GetInt64(12);
                            if (!reader.IsDBNull(13))
                                p.UserName = reader.GetString(13);

                            r.Content = p;
                        }
                    }
                }
                con.Close();
            }
            catch (Exception ex)
            {
                r.Code = ID_UNKNOWN_ERROR;
                r.Message = ex.Message;
            }

            return r;
        }

        public ResultPlaces GetNearPlaces(double latitude, double longitude)
        {
            ResultPlaces r = new ResultPlaces();

            // Validate if login is valid
            if (latitude == 0 || longitude == 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            try
            {
                SqlConnection con;
                using (SqlCommand command = new SqlCommand("get_near_places", con = getConnection()))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    SqlCommandBuilder.DeriveParameters(command);

                    command.Parameters["@latitude"].Value = latitude;
                    command.Parameters["@longitude"].Value = longitude;

                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        List<Place> places = new List<Place>();

                        while (reader.Read())
                        {
                            Place p = new Place();

                            if (!reader.IsDBNull(0))
                                p.Id = reader.GetInt64(0);
                            if (!reader.IsDBNull(1))
                                p.Latitude = reader.GetDouble(1);
                            if (!reader.IsDBNull(2))
                                p.Longitude = reader.GetDouble(2);
                            if (!reader.IsDBNull(3))
                                p.Type = reader.GetInt64(3);
                            if (!reader.IsDBNull(4))
                                p.Name = reader.GetString(4);
                            if (!reader.IsDBNull(5))
                                p.Description = reader.GetString(5);
                            if (!reader.IsDBNull(6))
                                p.Date = reader.GetDateTime(6).ToString("dd/MM/yyyy HH:mm");
                            if (!reader.IsDBNull(7))
                                p.Points = reader.GetInt64(7);
                            if (!reader.IsDBNull(8))
                                p.Checkins = reader.GetInt64(8);
                            if (!reader.IsDBNull(9))
                                p.Subjects = reader.GetInt64(9);
                            if (!reader.IsDBNull(10))
                                p.Likes = reader.GetInt64(10);
                            if (!reader.IsDBNull(11))
                                p.Dislikes = reader.GetInt64(11);
                            if (!reader.IsDBNull(12))
                                p.UserId = reader.GetInt64(12);
                            if (!reader.IsDBNull(13))
                                p.UserName = reader.GetString(13);

                            places.Add(p);
                        }

                        Place[] ps = new Place[places.Count];

                        for (int i = 0; i < ps.Length; i++)
                            ps[i] = places[i];

                        r.Content = ps;
                    }
                }
                con.Close();
            }
            catch (Exception ex)
            {
                r.Code = ID_UNKNOWN_ERROR;
                r.Message = ex.Message;
            }

            return r;
        }

        public ResultSubject GetSubject(long id)
        {
            ResultSubject r = new ResultSubject();

            // Validate if login is valid
            if (id <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("get_subject", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@id"].Value = id;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    List<Subject> subjects = new List<Subject>();

                    if (reader.Read())
                    {
                        Subject s = new Subject();

                        if (!reader.IsDBNull(0))
                            s.Id = reader.GetInt64(0);
                        if (!reader.IsDBNull(1))
                            s.PlaceId = reader.GetInt64(1);
                        if (!reader.IsDBNull(2))
                            s.Date = reader.GetDateTime(2).ToString("dd/MM/yyyy HH:mm");
                        if (!reader.IsDBNull(3))
                            s.Content = reader.GetString(3);
                        if (!reader.IsDBNull(4))
                            s.Points = reader.GetInt64(4);
                        if (!reader.IsDBNull(5))
                            s.Comments = reader.GetInt64(5);
                        if (!reader.IsDBNull(6))
                            s.Likes = reader.GetInt64(6);
                        if (!reader.IsDBNull(7))
                            s.Dislikes = reader.GetInt64(7);
                        if (!reader.IsDBNull(8))
                            s.UserId = reader.GetInt64(8);
                        if (!reader.IsDBNull(9))
                            s.UserName = reader.GetString(9);

                        r.Content = s;
                    }
                }
            }
            con.Close();

            return r;
        }

        public ResultSubjects GetSubjects(long place)
        {
            ResultSubjects r = new ResultSubjects();

            // Validate if login is valid
            if (place <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("get_subjects", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@placeId"].Value = place;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    List<Subject> subjects = new List<Subject>();

                    while (reader.Read())
                    {
                        Subject s = new Subject();

                        if (!reader.IsDBNull(0))
                            s.Id = reader.GetInt64(0);
                        if (!reader.IsDBNull(1))
                            s.PlaceId = reader.GetInt64(1);
                        if (!reader.IsDBNull(2))
                            s.Date = reader.GetDateTime(2).ToString("dd/MM/yyyy HH:mm");
                        if (!reader.IsDBNull(3))
                            s.Content = reader.GetString(3);
                        if (!reader.IsDBNull(4))
                            s.Points = reader.GetInt64(4);
                        if (!reader.IsDBNull(5))
                            s.Comments = reader.GetInt64(5);
                        if (!reader.IsDBNull(6))
                            s.Likes = reader.GetInt64(6);
                        if (!reader.IsDBNull(7))
                            s.Dislikes = reader.GetInt64(7);
                        if (!reader.IsDBNull(8))
                            s.UserId = reader.GetInt64(8);
                        if (!reader.IsDBNull(9))
                            s.UserName = reader.GetString(9);

                        subjects.Add(s);
                    }

                    Subject[] ss = new Subject[subjects.Count];

                    for (int i = 0; i < ss.Length; i++)
                        ss[i] = subjects[i];

                    r.Content = ss;
                }
            }
            con.Close();

            return r;
        }

        public ResultComment GetComment(long id)
        {
            ResultComment r = new ResultComment();

            // Validate if login is valid
            if (id <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("get_comment", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@id"].Value = id;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    List<Comment> comments = new List<Comment>();

                    if (reader.Read())
                    {
                        Comment c = new Comment();

                        if (!reader.IsDBNull(0))
                            c.Id = reader.GetInt64(0);
                        if (!reader.IsDBNull(1))
                            c.SubjectId = reader.GetInt64(1);
                        if (!reader.IsDBNull(2))
                            c.Date = reader.GetDateTime(2).ToString("dd/MM/yyyy HH:mm");
                        if (!reader.IsDBNull(3))
                            c.Content = reader.GetString(3);
                        if (!reader.IsDBNull(4))
                            c.Points = reader.GetInt64(4);
                        if (!reader.IsDBNull(5))
                            c.Answers = reader.GetInt64(5);
                        if (!reader.IsDBNull(6))
                            c.Likes = reader.GetInt64(6);
                        if (!reader.IsDBNull(7))
                            c.Dislikes = reader.GetInt64(7);
                        if (!reader.IsDBNull(8))
                            c.UserId = reader.GetInt64(8);
                        if (!reader.IsDBNull(9))
                            c.UserName = reader.GetString(9);

                        r.Content = c;
                    }
                }
            }
            con.Close();

            return r;
        }

        public ResultComments GetComments(long subject)
        {
            ResultComments r = new ResultComments();

            // Validate if login is valid
            if (subject <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("get_comments", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@subjectId"].Value = subject;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    List<Comment> comments = new List<Comment>();

                    while (reader.Read())
                    {
                        Comment c = new Comment();

                        if (!reader.IsDBNull(0))
                            c.Id = reader.GetInt64(0);
                        if (!reader.IsDBNull(1))
                            c.SubjectId = reader.GetInt64(1);
                        if (!reader.IsDBNull(2))
                            c.Date = reader.GetDateTime(2).ToString("dd/MM/yyyy HH:mm");
                        if (!reader.IsDBNull(3))
                            c.Content = reader.GetString(3);
                        if (!reader.IsDBNull(4))
                            c.Points = reader.GetInt64(4);
                        if (!reader.IsDBNull(5))
                            c.Answers = reader.GetInt64(5);
                        if (!reader.IsDBNull(6))
                            c.Likes = reader.GetInt64(6);
                        if (!reader.IsDBNull(7))
                            c.Dislikes = reader.GetInt64(7);
                        if (!reader.IsDBNull(8))
                            c.UserId = reader.GetInt64(8);
                        if (!reader.IsDBNull(9))
                            c.UserName = reader.GetString(9);

                        comments.Add(c);
                    }

                    Comment[] cs = new Comment[comments.Count];

                    for (int i = 0; i < cs.Length; i++)
                        cs[i] = comments[i];

                    r.Content = cs;
                }
            }
            con.Close();

            return r;
        }

        public ResultAnswer GetAnswer(long id)
        {
            ResultAnswer r = new ResultAnswer();

            // Validate if login is valid
            if (id <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("get_answer", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@id"].Value = id;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    List<Answer> answers = new List<Answer>();

                    if (reader.Read())
                    {
                        Answer a = new Answer();

                        if (!reader.IsDBNull(0))
                            a.Id = reader.GetInt64(0);
                        if (!reader.IsDBNull(1))
                            a.CommentId = reader.GetInt64(1);
                        if (!reader.IsDBNull(2))
                            a.Date = reader.GetDateTime(2).ToString("dd/MM/yyyy HH:mm");
                        if (!reader.IsDBNull(3))
                            a.Content = reader.GetString(3);
                        if (!reader.IsDBNull(4))
                            a.Likes = reader.GetInt64(4);
                        if (!reader.IsDBNull(5))
                            a.Dislikes = reader.GetInt64(5);
                        if (!reader.IsDBNull(6))
                            a.UserId = reader.GetInt64(6);
                        if (!reader.IsDBNull(7))
                            a.UserName = reader.GetString(7);

                        r.Content = a;
                    }
                }
            }
            con.Close();

            return r;
        }

        public ResultAnswers GetAnswers(long comment)
        {
            ResultAnswers r = new ResultAnswers();

            // Validate if login is valid
            if (comment <= 0)
            {
                r.Code = ID_INVALID_CONTENT;
                r.Message = DESCRIPTION_INVALID_CONTENT;
                return r;
            }

            SqlConnection con;
            using (SqlCommand command = new SqlCommand("get_answers", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@commentId"].Value = comment;

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    List<Answer> answers = new List<Answer>();

                    while (reader.Read())
                    {
                        Answer a = new Answer();

                        if (!reader.IsDBNull(0))
                            a.Id = reader.GetInt64(0);
                        if (!reader.IsDBNull(1))
                            a.CommentId = reader.GetInt64(1);
                        if (!reader.IsDBNull(2))
                            a.Date = reader.GetDateTime(2).ToString("dd/MM/yyyy HH:mm");
                        if (!reader.IsDBNull(3))
                            a.Content = reader.GetString(3);
                        if (!reader.IsDBNull(4))
                            a.Likes = reader.GetInt64(4);
                        if (!reader.IsDBNull(5))
                            a.Dislikes = reader.GetInt64(5);
                        if (!reader.IsDBNull(6))
                            a.UserId = reader.GetInt64(6);
                        if (!reader.IsDBNull(7))
                            a.UserName = reader.GetString(7);

                        answers.Add(a);
                    }

                    Answer[] ans = new Answer[answers.Count];

                    for (int i = 0; i < ans.Length; i++)
                        ans[i] = answers[i];

                    r.Content = ans;
                }
            }
            con.Close();

            return r;
        }
        #endregion

        #region Extras
        private const string connection_string = "Server=54.242.85.210;Integrated Security=False;Database=Legion;UId='external';Pwd='abc123';";
        private static SqlConnection getConnection()
        {
            SqlConnection sqlConnection = new SqlConnection(connection_string);
            sqlConnection.Open();

            return sqlConnection;
        }
        #endregion
    }
}
