const database = require('../../database/config');
const status = require('../../utilities/server_status');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserFollowing = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const id = req.body.id;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const query = `SELECT DISTINCT users.name, 
                    users.username,
                    users.email,
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

    const args = [
        id,
        count,
        offset
    ];

    database.query(query, args, (err, result) => {
        if(err) throw err;
        res.status(status.OK).json(result); 
    });
};

exports.getUserFollowers = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const id = req.body.id;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const query = "SELECT * FROM follows WHERE toUser = ? LIMIT ? OFFSET ?";

    const args = [
        id,
        count,
        offset
    ];

    database.query(query, args, (err, result) => {
        if(err) throw err;
        res.status(status.OK).json(result);
    });
};

exports.followUser = (req, res) => {
    const fromUser = req.body.fromUser;
    const toUser = req.body.toUser;

    const query = 'INSERT INTO follows(fromUser, toUser) VALUES(?, ?)';
    const args = [
        fromUser,
        toUser
    ];

    database.query(query, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Follow is done",
            });
        }
        else {
            res.status(status.BAD_REQUEST).json({
                message: "Invalid Follow",
            });
        }
    });
};

exports.unFollowUser = (req, res) => {
    const fromUser = req.body.fromUser;
    const toUser = req.body.toUser;

    const query = 'DELETE FROM follows WHERE fromUser = ? AND toUser = ?';

    const args = [
        fromUser,
        toUser
    ];

    database.query(query, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Un Follow is done",
            });
        }
        else {
            res.status(status.BAD_REQUEST).json({
                message: "Invalid un Follow",
            });
        }
    });
};