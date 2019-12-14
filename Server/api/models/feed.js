const database = require('../../database/config');

exports.getUserFeed = (args) => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT answers.id, 
                                   (SELECT title FROM questions WHERE questionId = questions.id) AS questionBody,
                                   answers.body AS answerBody,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUsername,
                                   (SELECT email FROM users WHERE fromUser = users.id) AS fromUserEmail,
                                   (SELECT avatar FROM users WHERE fromUser = users.id) AS fromUserAvatar,
                                   (SELECT username FROM users WHERE toUser = users.id) AS toUsername,
                                   (SELECT email FROM users WHERE toUser = users.id) AS toUserEmail,
                                   (SELECT avatar FROM users WHERE toUser = users.id) AS toUserAvatar,
                                   (SELECT COUNT(*) FROM reactions WHERE answerId = answers.id) AS reactions,
                                   answers.answerdDate
                    FROM answers WHERE fromUser = ? LIMIT ? OFFSET ?`;
    database.query(query, args, (err, result) => {
       if(err) throw err;
       resolve(result)
    });
});