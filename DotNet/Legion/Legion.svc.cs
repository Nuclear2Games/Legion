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
                        r.Content = reader.GetInt64(0);
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

        public ResultId Login(string login, string password)
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
                        r.Content = reader.GetInt64(0);
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
        public ResultId NewPlace(long user, double latitude, double longitude, long type, string name, string description)
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

                using (SqlDataReader reader = command.ExecuteReader())
                {
                    // If there is result, the ID will be returned
                    if (reader.Read())
                        r.Content = reader.GetInt64(0);
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

        public Result UpdatePlace(long user, long place, double latitude, double longitude, string name, string description)
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
            using (SqlCommand command = new SqlCommand("update_user", con = getConnection()))
            {
                command.CommandType = System.Data.CommandType.StoredProcedure;
                SqlCommandBuilder.DeriveParameters(command);

                command.Parameters["@id"].Value = place;
                command.Parameters["@userId"].Value = user;
                command.Parameters["@latitude"].Value = latitude;
                command.Parameters["@longitude"].Value = longitude;
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
                        r.Content = reader.GetInt64(0);
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
                        r.Content = reader.GetInt64(0);
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
            using (SqlCommand command = new SqlCommand("new_comment", con = getConnection()))
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
                        r.Content = reader.GetInt64(0);
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
                        u.Login = reader.GetString(0);
                        u.Name = reader.GetString(1);
                        u.Description = reader.GetString(2);
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

                            n.Id = reader.GetInt64(0);
                            n.CustomId = reader.GetInt64(1);
                            n.CustomTypeId = reader.GetByte(2);
                            n.What = reader.GetInt64(3);
                            n.Date = reader.GetDateTime(4);
                            n.Seen = reader.GetBoolean(5);

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

        public ResultPlaces GetNearPlaces(string latitude, string longitude)
        {
            ResultPlaces r = new ResultPlaces();

            double lat;
            double lon;
            try
            {
                lat = Double.Parse(latitude);
                lon = Double.Parse(longitude);
            }
            catch
            {
                lat = 0;
                lon = 0;
            }

            // Validate if login is valid
            if (lat == 0 || lon == 0)
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

                            p.Id = reader.GetInt64(0);
                            p.Latitude = reader.GetDouble(1);
                            p.Longitude = reader.GetDouble(2);
                            p.Type = reader.GetInt64(3);
                            p.Name = reader.GetString(4);
                            p.Description = reader.GetString(5);
                            p.Date = reader.GetDateTime(6);
                            p.Points = reader.GetInt64(7);
                            p.Subjects = reader.GetInt64(8);
                            p.Likes = reader.GetInt64(9);
                            p.Dislikes = reader.GetInt64(10);
                            p.UserId = reader.GetInt64(11);
                            p.UserName = reader.GetString(12);

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

                        s.Id = reader.GetInt64(0);
                        s.PlaceId = reader.GetInt64(1);
                        s.Date = reader.GetDateTime(2);
                        s.Content = reader.GetString(3);
                        s.Points = reader.GetInt64(4);
                        s.Comments = reader.GetInt64(5);
                        s.Likes = reader.GetInt64(6);
                        s.Dislikes = reader.GetInt64(7);
                        s.UserId = reader.GetInt64(8);
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

                        c.Id = reader.GetInt64(0);
                        c.SubjectId = reader.GetInt64(1);
                        c.Date = reader.GetDateTime(2);
                        c.Content = reader.GetString(3);
                        c.Points = reader.GetInt64(4);
                        c.Answers = reader.GetInt64(5);
                        c.Likes = reader.GetInt64(6);
                        c.Dislikes = reader.GetInt64(7);
                        c.UserId = reader.GetInt64(8);
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

                        a.Id = reader.GetInt64(0);
                        a.CommentId = reader.GetInt64(1);
                        a.Date = reader.GetDateTime(2);
                        a.Content = reader.GetString(3);
                        a.Likes = reader.GetInt64(4);
                        a.Dislikes = reader.GetInt64(5);
                        a.UserId = reader.GetInt64(6);
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
