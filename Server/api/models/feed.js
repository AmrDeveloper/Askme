const database = require('../../database/config');

exports.getUserFeed = (args) => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT answers.id AS answerId, 
                                   (SELECT title FROM questions WHERE questionId = questions.id) AS questionBody,
                                   answers.body AS answerBody,
                                   (SELECT id FROM users WHERE fromUser = users.id) AS fromUserId,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUsername,
                                   (SELECT email FROM users WHERE fromUser = users.id) AS fromUserEmail,
                                   (SELECT avatar FROM users WHERE fromUser = users.id) AS fromUserAvatar,
                                   (SELECT id FROM users WHERE toUser = users.id) AS toUserId,
                                   (SELECT username FROM users WHERE toUser = users.id) AS toUsername,
                                   (SELECT email FROM users WHERE toUser = users.id) AS toUserEmail,
                                   (SELECT avatar FROM users WHERE toUser = users.id) AS toUserAvatar,
                                   (SELECT anonymous FROM questions WHERE questionId = questions.id) as anonymous,
                                   (SELECT COUNT(*) FROM reactions WHERE answerId = answers.id) AS reactions,
                                   (SELECT IF(COUNT(*) >= 1, TRUE, FALSE) FROM reactions WHERE answerId = answers.id AND fromUser = ?) AS isReacted,
                                   answers.answerdDate AS answerDate
                    FROM answers WHERE fromUser = ? LIMIT ? OFFSET ?`;
    database.query(query, args, (err, result) => {
       if(err) throw err;
       resolve(result)
    });
});

exports.getHomeFeed = (args) => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT answers.id AS answerId, 
                                    (SELECT title FROM questions WHERE questionId = questions.id) AS questionBody,
                                    answers.body AS answerBody,
                                    (SELECT id FROM users WHERE answers.fromUser = users.id) AS fromUserId,
                                    (SELECT username FROM users WHERE answers.fromUser = users.id) AS fromUsername,
                                    (SELECT email FROM users WHERE answers.fromUser = users.id) AS fromUserEmail,
                                    (SELECT avatar FROM users WHERE answers.fromUser = users.id) AS fromUserAvatar,
                                    (SELECT id FROM users WHERE answers.toUser = users.id) AS toUserId,
                                    (SELECT username FROM users WHERE answers.toUser = users.id) AS toUsername,
                                    (SELECT email FROM users WHERE answers.toUser = users.id) AS toUserEmail,
                                    (SELECT avatar FROM users WHERE answers.toUser = users.id) AS toUserAvatar,
                                    (SELECT anonymous FROM questions WHERE questionId = questions.id) as anonymous,
                                    (SELECT COUNT(*) FROM reactions WHERE answerId = answers.id) AS reactions,
                                    (SELECT IF(COUNT(*) >= 1, TRUE, FALSE) FROM reactions WHERE answerId = answers.id AND fromUser = ?) AS isReacted,
                                    answers.answerdDate AS answerDate
                   FROM answers JOIN follows 
                   ON answers.fromUser = follows.toUser AND follows.fromUser = ?
                   LIMIT ? OFFSET ?`;  
                                 
    database.query(query, args, (err, result) => {
       if(err) throw err;
       resolve(result)
    });
});