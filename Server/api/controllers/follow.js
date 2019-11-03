const database = require('../../database/config');
const status = require('../../utilities/server_status');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserFollowing = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    res.status(200).json({
        message: "Get all user following"
    })
};

exports.getUserFollowers = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    res.status(200).json({
        message: "Get all user followers"
    })
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
        if(err) throw err;
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

    const query = 'DELETE * FROM follows WHERE fromUser = ? AND toUser = ?';

    const args = [
        fromUser,
        toUser
    ];

    database.query(query, args, (err, result) => {
        if(err) throw err;
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