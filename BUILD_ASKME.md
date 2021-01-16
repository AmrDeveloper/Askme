### Build Askme Project

You need to have NodeJs and MySQL Installed

To get your NodeJs version try

```
node --version
```

Go to Server directory

```
cd Server
```

Ceate directories for storage/avatar and storage/wallpaper

```
mkdir storage
cd storage
mkdir avatar
mkdir wallpaper
```

Install Server Dependencies

```
npm install
```

Open PhpMyAdmin and create new database with name askme
Then just run the server

```
npm start
```

To find your IPv4 Address try

```
ipconfig
```

In Askme Android App open Consts File and change API_SERVER_HOST value with your current IPv4 Address value
Then just run the Android App

Enjoy