const mysql = require('mysql');

const connectionPool = mysql.createPool(
    {
        connectionLimit : 10,
        host : 'localhost',
        user : 'root',
        password : '',
        database : 'askme',

    }
);

module.exports = connectionPool;