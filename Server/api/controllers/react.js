const database = require('../../database/config');
const status = require('../../utilities/server_status');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getPostReactions = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const answerId = req.body.answerId;
    const sqlQuery = `SELECT DISTINCT 
                     reactions.react
                     (SELECT username, avatar FROM users WHERE users.id = fromUser),
                     FROM reactions WHERE answerId = ? LIMIT ? OFFSET ?`;

    const args = [
        answerId,
        count,
        offset
    ];

    database.query(sqlQuery, args, (err, result) => {
        if (err) throw err;
        res.status(status.OK).json(result);
    });
};

exports.createNewReaction = (req, res) => {
    const userId = req.body.userId;
    const answerId = req.body.answerId;
    const reactionsType = req.body.reactionsType;

    const sqlQuery = 'INSERT INTO reactions (fromUser, answerId, react) VALUES (?, ?, ?)';

    const args = [
        userId,
        answerId,
        reactionsType
    ];

    database.query(sqlQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Reaction is Created",
            });
        }
        else {
            res.status(status.BAD_REQUEST).json({
                message: "Reaction not Created",
            });
        }
    });
};

exports.deleteReaction = (req, res) => {
    const userId = req.body.userId;
    const answerId = req.body.answerId;

    const sqlQuery = 'DELETE FROM reactions WHERE fromUser = ? AND answerId = ?';

    const args = [
        userId,
        answerId
    ];

    database.query(sqlQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Reaction is deleted",
            });
        }
        else {
            res.status(status.BAD_REQUEST).json({
                message: "Reaction not deleted",
            });
        }
    });
};