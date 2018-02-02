# PrakarshApp
Application for [Prakarsh XIII (2018)](https://prakarsh.org) Fest.

[Play Store](https://play.google.com/store/apps/details?id=in.ac.svit.prakarsh) |	
[Github Releases](https://github.com/itsarjunsinh/PrakarshApp/releases)

This app uses JSON files from [PrakarshJSON](https://github.com/SvitPrakarsh/PrakarshJSON) project hosted with Github Pages.


## Firebase Configuration

#### Firestore Database Structure 

users -> [Unique User ID] -> (fields) city, collegeName, department, email, name, phoneNumber

*Create "users" collection using the Firebase  console. THe UUID documents and the details fields will automatically be generated from the client side.*

#### Firestore Database Rules

```
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userID} {
      allow create, update, read: if request.auth.uid == userID;
    }
  }
}
```
