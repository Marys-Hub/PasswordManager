# PasswordManager
Resembling Apps: Bitwerden, Keychain on IOS 
-tabel apps similare 
**Important Features** : 
-password storage, 
-password generator,
-secure sharing- NOPE  
-data encryption- OPTIONAL  
-security audit (The app includes a security audit feature that evaluates the strength and security of stored passwords and offers suggestions for improving security.)
- offline access
- search and filtering
- cross platform; will only support Android
- URL recognition and autofill - OPTIONAL
- Customisation: Users can customise the app's appearance, including themes and icons.
- Hide content for sensitive data before clicked
- face recognition
- captcha :  plimbare in areal- unic pass
-  locattion login analysis


Database Design Password Manager:

User Table: PK is userID 
userId (Primary Key): A unique identifier for each user.
email (Unique): The user's primary email address, which serves as the primary identifier for each user.
masterPassword: The master password used to access the password manager and later on to see stored passwords.



User Credentials Table: PK is userId and websiteURL 
userId (Foreign Key): References the userId in the User Table, linking user credentials to the user via their email address.
websiteName: The name or title of the website, which is manually entered by the user when deciding to save credentials.
username: The username or email associated with the account on the website.- can be different cause it does not affect 
password: The password for the account on the website.
websiteURL: The URL address of the website.
