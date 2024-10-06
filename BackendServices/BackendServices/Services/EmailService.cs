using System.Net;
using System.Net.Mail;

namespace BackendServices.Services
{
    public class EmailService
    {
        private readonly SmtpClient _client;
        private readonly string _fromEmail;

        public EmailService()
        {
            // Initialize SMTP client and email configurations (keep this as is)
            _client = new SmtpClient("bulk.smtp.mailtrap.io", 587)
            {
                Credentials = new NetworkCredential("smtp@mailtrap.io", "2c60006e49265889677f41b8d86cff98"),
                EnableSsl = true
            };
            
        }

        // The method with parameters for subject and body
        public static void SendCustomEmail(string toEmail, string subject, string body)
        {
            var client = new SmtpClient("bulk.smtp.mailtrap.io", 587)
            {
                Credentials = new NetworkCredential("smtp@mailtrap.io", "2c60006e49265889677f41b8d86cff98"),
                EnableSsl = true
            };
            client.Send("hello@demomailtrap.com", toEmail, subject, body);
            System.Console.WriteLine("Sent");   
        }
    }
}