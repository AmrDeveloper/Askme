const mysql = require('mysql');

const databaseConfig = {
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'askme',
};

const connection = mysql.createConnection(databaseConfig);

const MYSQL_USERS_TABLE = `CREATE TABLE IF NOT EXISTS users(
    id INTEGER, 
    name TEXT,
    username TEXT,
    email TEXT,
    password TEXT,
    avatar TEXT,
    address TEXT,
    status TEXT,
    active CHAR(1),
    PRIMARY KEY(id),
    UNIQUE(email, username)
)`;

connection.connect(error => {
    if (error) throw error;

    console.debug("Open MySQL Connection");

    //Create users table
    connection.query(MYSQL_USERS_TABLE, (err, res) =>{
        if (err) throw err;
    });
});

module.exports = connection;