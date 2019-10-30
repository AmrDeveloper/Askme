const mysql = require('mysql');

const databaseConfig = {
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'askme',
};

const connection = mysql.createConnection(databaseConfig);

connection.connect(error => {
    if (error) throw error;
    console.log("Open MySQL Connection")

    const mysqlCreation = "CREATE DATABASE IF NOT EXISTS askme";
    connection.query(mysqlCreation, (error, result) => {
        if (error) throw error;  
        console.log("Database created"); 
    });

    connection.end(error => {
        if (error) throw error;
        console.log("Close MySQL Connection")
    })
});

module.exports = connection;