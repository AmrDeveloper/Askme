const database = require('../../database/config');

exports.getAnswerByID = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT id,
                                   body,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUser,
                                   (SELECT avatar FROM users WHERE fromUser = users.id) AS avatar,
                                   answerdDate
                                   FROM answers WHERE id = ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.getQuestionAnswer = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT id,
                                   toUser AS toUserId,
                                   (SELECT username FROM users WHERE toUser = users.id) AS toUsername, 
                                   (SELECT avatar FROM users WHERE toUser = users.id) AS toUserAvatar,
                                   fromUser AS fromUserId,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUsername, 
                                   (SELECT avatar FROM users WHERE toUser = users.id) AS fromUserAvatar,
                                   questionId,
                                   (SELECT title FROM questions WHERE questionId = questions.id) AS fromUser,
                                   (SELECT anonymous FROM questions WHERE questionId = questions.id) AS isAnonymous,
                                   answerdDate
                                   FROM answers WHERE id = ?`;
    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.createNewAnswer = args => new Promise((resolve, reject) => {
    const query = 'INSERT INTO answers(body, questionId, toUser, fromUser, answerdDate) VALUES (?, ?, ?, ?, ?)';

    database.query(query, args, (err, result) => {
        if (err) throw err;
        const isValidRequest = result['affectedRows'] == 1;
        resolve([isValidRequest, result.insertId]);
    });
});

exports.deleteAnswer = args => new Promise((resolve, reject) => {
    const query = 'DELETE FROM answers WHERE id = ?';

    database.query(query, args, (err, result => {
        if (err) throw err;
        const isValidRequest = result['affectedRows'] > 0;
        resolve(isValidRequest);
    }));
});