const functions = require('firebase-functions');
const nodemailer = require('nodemailer');

// Configure your email transport
const transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: 'your-email@gmail.com',
    pass: 'your-email-password',
  },
});

exports.sendEmail = functions.https.onCall((data, context) => {
  const { to, subject, body } = data;

  const mailOptions = {
    from: 'your-email@gmail.com',
    to,
    subject,
    text: body,
  };

  return transporter.sendMail(mailOptions)
    .then(() => {
      return { success: true };
    })
    .catch((error) => {
      console.error(error);
      throw new functions.https.HttpsError('internal', 'Failed to send email');
    });
});

