const database = require('../../database/config');

exports.getPostReactions = args => new Promise((resolve, reject) => {
    const sqlQuery = `SELECT DISTINCT 
                            reactions.react
                            (SELECT username, avatar FROM users WHERE users.id = fromUser),
                            FROM reactions WHERE answerId = ? LIMIT ? OFFSET ?`;


    database.query(sqlQuery, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.createNewReaction = args => new Promise((resolve, reject) => {
    const sqlQuery = 'INSERT INTO reactions (fromUser, toUser, answerId, react) VALUES (?, ?, ?, ?)';

    database.query(sqlQuery, args, (err, result) => {
        if (err) {
            if (err.code === 'ER_DUP_ENTRY') {
                resolve(false);
            } else {
                throw err;
            }
        } else {
            const isValidRequest = result['affectedRows'] == 1;
            resolve(isValidRequest);
        }
    });
});

exports.deleteReaction = args => new Promise((resolve, reject) => {
    const sqlQuery = 'DELETE FROM reactions WHERE fromUser = ? AND answerId = ?';

    database.query(sqlQuery, args, (err, result) => {
        if (err) throw err;
        const isValidRequest = result['affectedRows'] == 1;
        resolve(isValidRequest);
    });
});