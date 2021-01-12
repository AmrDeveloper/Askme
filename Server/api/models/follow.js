const database = require('../../database/config');

exports.getUserFollowing = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT users.name, 
                    users.username,
                    users.email,
                    users.avatar,
                    users.address,
                    users.status,
                    users.active,
                    users.joinDate,
                    (SELECT COUNT(*) FROM follows WHERE fromUser = users.id) AS following,
                    (SELECT COUNT(*) FROM follows WHERE toUser = users.id) AS followers,
                    (SELECT COUNT(*) FROM questions WHERE fromUser = users.id) AS questions,
                    (SELECT COUNT(*) FROM answers WHERE fromUser = users.id) AS answers,
                    (SELECT COUNT(*) FROM reactions WHERE fromUser = users.id) AS likes
                    FROM users JOIN follows
                    ON users.id = follows.toUser
                    WHERE follows.fromUser = ? LIMIT ? OFFSET ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.getUserFollowers = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT users.name, 
                    users.username,
                    users.email,
                    users.avatar,
                    users.address,
                    users.status,
                    users.active,
                    users.joinDate,
                    (SELECT COUNT(*) FROM follows WHERE fromUser = users.id) AS following,
                    (SELECT COUNT(*) FROM follows WHERE toUser = users.id) AS followers,
                    (SELECT COUNT(*) FROM questions WHERE fromUser = users.id) AS questions,
                    (SELECT COUNT(*) FROM answers WHERE fromUser = users.id) AS answers,
                    (SELECT COUNT(*) FROM reactions WHERE fromUser = users.id) AS likes
                    FROM users JOIN follows
                    ON users.id = follows.fromUser
                    WHERE follows.toUser = ? LIMIT ? OFFSET ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.followUser = args => new Promise((resolve, reject) => {
    const query = 'INSERT INTO follows(fromUser, toUser) VALUES(?, ?)';

    database.query(query, args, (err, result) => {
        if (err) {
            if (err.code == 'ER_DUP_ENTRY' || err.errno == 1062) {
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

exports.unfollowUser = args => new Promise((resolve, reject) => {
    const query = 'DELETE FROM follows WHERE fromUser = ? AND toUser = ?';

    database.query(query, args, (err, result) => {
        if (err) throw err;
        const isValidRequest = result['affectedRows'] == 1;
        resolve(isValidRequest);
    });
});