using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace ManyFest
{
    [ServiceContract]
    public interface IManyFest
    {
        #region User

        [OperationContract]
        [WebInvoke(UriTemplate = "NewUser", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result NewUser(string login, string password);

        [OperationContract]
        [WebInvoke(UriTemplate = "Login", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result Login(string login, string password);

        [OperationContract]
        [WebInvoke(UriTemplate = "UpdateUser", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result UpdateUser(long id, string name, string description);

        [OperationContract]
        [WebInvoke(UriTemplate = "UpdatePassword", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result UpdatePassword(long id, string passwordOld, string passwordNew);

        #endregion

        #region Place
        [OperationContract]
        [WebInvoke(UriTemplate = "NewPlace", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result NewPlace(long user, double latitude, double longitude, long type, string name, string description);

        [OperationContract]
        [WebInvoke(UriTemplate = "UpdatePlace", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result UpdatePlace(long user, long place, double latitude, double longitude, string name, string description);
        #endregion

        // TODO: routes

        #region Subjects

        [OperationContract]
        [WebInvoke(UriTemplate = "NewSubject", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result NewSubject(long user, long place, string content);

        #endregion

        #region Comments

        [OperationContract]
        [WebInvoke(UriTemplate = "NewComment", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result NewComment(long user, long place, long subject, string content);

        #endregion

        #region Answers

        [OperationContract]
        [WebInvoke(UriTemplate = "NewAnswer", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result NewAnswer(long user, long place, long subject, long comment, string content);

        #endregion

        #region Gets...

        [OperationContract]
        [WebInvoke(UriTemplate = "GetUser", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result GetUser(long user);

        [OperationContract]
        [WebInvoke(UriTemplate = "GetNearPlaces", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result GetNearPlaces(double latitude, double longitude);

        // TODO: routes

        [OperationContract]
        [WebInvoke(UriTemplate = "GetSubjects", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result GetSubjects(long place);

        [OperationContract]
        [WebInvoke(UriTemplate = "GetComments", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result GetComments(long subject);

        [OperationContract]
        [WebInvoke(UriTemplate = "GetAnswers", Method = "POST", ResponseFormat = WebMessageFormat.Json, RequestFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped)]
        Result GetAnswers(long comment);

        #endregion
    }

    #region Return
    [DataContract]
    public class Result
    {
        public Result()
        {
            Code = 1;
            Message = "";
            Content = null;
        }

        [DataMember]
        public int Code { get; set; }
        [DataMember]
        public string Message { get; set; }
        [DataMember]
        public Object Content { get; set; }
    }
    #endregion

    #region User
    [DataContract]
    public class User
    {
        private long id;
        private string login;
        private string name;
        private string description;
        private long points;

        [DataMember]
        public long Id
        {
            get { return id; }
            set { id = value; }
        }

        [DataMember]
        public string Login
        {
            get { return login; }
            set { login = value; }
        }

        [DataMember]
        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        [DataMember]
        public string Description
        {
            get { return description; }
            set { description = value; }
        }

        [DataMember]
        public long Points
        {
            get { return points; }
            set { points = value; }
        }
    }
    #endregion

    #region Place
    [DataContract]
    public class Place
    {
        private long id;
        private long userId;
        private string userName;
        private double latitude;
        private double longitude;
        private long type;
        private string name;
        private string description;
        private DateTime date;
        private long points;
        private long subjects;
        private long likes;
        private long dislikes;

        [DataMember]
        public long Id
        {
            get { return id; }
            set { id = value; }
        }

        [DataMember]
        public long UserId
        {
            get { return userId; }
            set { userId = value; }
        }

        [DataMember]
        public string UserName
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public double Latitude
        {
            get { return latitude; }
            set { latitude = value; }
        }

        [DataMember]
        public double Longitude
        {
            get { return longitude; }
            set { longitude = value; }
        }

        [DataMember]
        public long Type
        {
            get { return type; }
            set { type = value; }
        }

        [DataMember]
        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        [DataMember]
        public string Description
        {
            get { return description; }
            set { description = value; }
        }

        [DataMember]
        public DateTime Date
        {
            get { return date; }
            set { date = value; }
        }

        [DataMember]
        public long Points
        {
            get { return points; }
            set { points = value; }
        }

        [DataMember]
        public long Subjects
        {
            get { return subjects; }
            set { subjects = value; }
        }

        [DataMember]
        public long Likes
        {
            get { return likes; }
            set { likes = value; }
        }

        [DataMember]
        public long Dislikes
        {
            get { return dislikes; }
            set { dislikes = value; }
        }
    }
    #endregion

    #region Subject
    [DataContract]
    public class Subject
    {
        private long id;
        private long placeId;
        private long userId;
        private string userName;
        private DateTime date;
        private string content;
        private long points;
        private long comments;
        private long likes;
        private long dislikes;

        [DataMember]
        public long Id
        {
            get { return id; }
            set { id = value; }
        }

        [DataMember]
        public long PlaceId
        {
            get { return placeId; }
            set { placeId = value; }
        }

        [DataMember]
        public long UserId
        {
            get { return userId; }
            set { userId = value; }
        }

        [DataMember]
        public string UserName
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public DateTime Date
        {
            get { return date; }
            set { date = value; }
        }

        [DataMember]
        public string Content
        {
            get { return content; }
            set { content = value; }
        }

        [DataMember]
        public long Points
        {
            get { return points; }
            set { points = value; }
        }

        [DataMember]
        public long Comments
        {
            get { return comments; }
            set { comments = value; }
        }
        
        [DataMember]
        public long Likes
        {
            get { return likes; }
            set { likes = value; }
        }

        [DataMember]
        public long Dislikes
        {
            get { return dislikes; }
            set { dislikes = value; }
        }
    }
    #endregion

    #region Comment
    [DataContract]
    public class Comment
    {
        private long id;
        private long subjectId;
        private long userId;
        private string userName;
        private DateTime date;
        private string content;
        private long points;
        private long answers;
        private long likes;
        private long dislikes;

        [DataMember]
        public long Id
        {
            get { return id; }
            set { id = value; }
        }

        [DataMember]
        public long SubjectId
        {
            get { return subjectId; }
            set { subjectId = value; }
        }

        [DataMember]
        public long UserId
        {
            get { return userId; }
            set { userId = value; }
        }

        [DataMember]
        public string UserName
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public DateTime Date
        {
            get { return date; }
            set { date = value; }
        }

        [DataMember]
        public string Content
        {
            get { return content; }
            set { content = value; }
        }

        [DataMember]
        public long Points
        {
            get { return points; }
            set { points = value; }
        }

        [DataMember]
        public long Answers
        {
            get { return answers; }
            set { answers = value; }
        }
        
        [DataMember]
        public long Likes
        {
            get { return likes; }
            set { likes = value; }
        }

        [DataMember]
        public long Dislikes
        {
            get { return dislikes; }
            set { dislikes = value; }
        }
    }
    #endregion

    #region Answers
    [DataContract]
    public class Answer
    {
        private long id;
        private long commentId;
        private long userId;
        private string userName;
        private DateTime date;
        private string content;
        private long likes;
        private long dislikes;

        [DataMember]
        public long Id
        {
            get { return id; }
            set { id = value; }
        }

        [DataMember]
        public long CommentId
        {
            get { return commentId; }
            set { commentId = value; }
        }

        [DataMember]
        public long UserId
        {
            get { return userId; }
            set { userId = value; }
        }

        [DataMember]
        public string UserName
        {
            get { return userName; }
            set { userName = value; }
        }

        [DataMember]
        public DateTime Date
        {
            get { return date; }
            set { date = value; }
        }

        [DataMember]
        public string Content
        {
            get { return content; }
            set { content = value; }
        }

        [DataMember]
        public long Likes
        {
            get { return likes; }
            set { likes = value; }
        }

        [DataMember]
        public long Dislikes
        {
            get { return dislikes; }
            set { dislikes = value; }
        }
    }
    #endregion
}
