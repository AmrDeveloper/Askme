const mysql = require('mysql');
const schema = require('../database/schema');

const databaseConfig = {
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'askme',
};

const connection = mysql.createConnection(databaseConfig);

connection.connect(error => {
    if (error) throw error;

    console.debug("Open MySQL Connection");

    //Create users table
    connection.query(schema.MYSQL_USERS_TABLE, (err, res) =>{
        if (err) throw err;
        console.log("Users Table Done");
    });

    //Create questions table
    connection.query(schema.MYSQL_QUESTIONS_TABLE, (err, res) =>{
        if (err) throw err;
        console.log("Questions Table Done");
    });
});

module.exports = connection;