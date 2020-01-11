const database = require('../../database/config');

exports.getUserQuestions = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT id,
                                   title,
                                   toUser AS toUserId,
                                   (SELECT username FROM users WHERE toUser = users.id) AS toUserName,
                                   fromUser AS fromUserId,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUserName,
                                   (SELECT avatar FROM users WHERE fromUser = users.id) AS fromUserAvatar,
                                   askedDate,
                                   anonymous
                                   FROM questions WHERE toUser = ? LIMIT ? OFFSET ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.getAskedQuestions = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT id,
                                   title,
                                   toUser AS toUserId,
                                   (SELECT username FROM users WHERE toUser = users.id) AS toUserName,
                                   fromUser AS fromUserId,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUserName,
                                   (SELECT avatar FROM users WHERE toUser = users.id) AS fromUserAvatar,
                                   askedDate,
                                   anonymous
                                   FROM questions WHERE fromUser = ? LIMIT ? OFFSET ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.getQuestionByID = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT id,
                                   title,
                                   toUser AS toUserId,
                                   (SELECT username FROM users WHERE toUser = users.id) AS toUserName,
                                   fromUser AS fromUserId,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUserName,
                                   (SELECT avatar FROM users WHERE fromUser = users.id) AS fromUserAvatar,
                                   anonymous,
                                   askedDate
                                   FROM questions WHERE id = ? LIMIT 1`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        if (result.length == 1) {
            resolve([true, result[0]]);
        } else {
            resolve([false]);
        }
    });
});

exports.createNewQuestion = args => new Promise((resolve, reject) => {
    const query = `INSERT INTO questions (title, toUser, fromUser, anonymous, askedDate) VALUES (?, ?, ?, ?, ?)`;

    database.query(query, args, (err, result) => {
        if(err){
            if (err.code === 'ER_DUP_ENTRY') {
                resolve([false]);
            } else {
                throw err;
            }
        }else{
            const isValidRequest = result['affectedRows'] == 1;
            resolve([isValidRequest, result.insertId]);
        }
    });
});

exports.deleteQuestion = args => new Promise((resolve, reject) => {
    const query = `DELETE FROM questions WHERE id = ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        const isValidRequest = result['affectedRows'] == 1;
        resolve(isValidRequest);
    });
});