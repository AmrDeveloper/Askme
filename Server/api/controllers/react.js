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

    const questionID = req.body.questionId;
    const sqlQuery = 'SELECT * FROM reactions WHEN questionId = ? LIMIT ? OFFSET ?';
    const args = [
        questionID,
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
    const questionId = req.body.questionId;
    const reactionsType = req.body.reactionsType;

    const sqlQuery = 'INSERT INTO reactions (fromUser, questionId, react) VALUES (?, ?, ?)';

    const args = [
        userId,
        questionId,
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
    const questionId = req.body.questionId;

    const sqlQuery = 'DELETE FROM reactions WHERE fromUser = ? AND questionId = ?';

    const args = [
        userId,
        questionId
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