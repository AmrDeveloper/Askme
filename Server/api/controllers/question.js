const database = require('../../database/config');
const status = require('../../utilities/server_status');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserQuestions = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const userId = req.params.id;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const query = `SELECT DISTINCT id,
                                   title,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUser,
                                   (SELECT avatar FROM users WHERE fromUser = users.id) AS avatar,
                                   askedDate,
                                   anonymous
                                   FROM questions WHERE toUser = ? LIMIT ? OFFSET ?`;

    const args = [
        userId,
        count,
        offset
    ];

    database.query(query, args, (err, result) => {
        if (err) throw err;
        res.status(status.OK).json(result);
    });
};

exports.getAskedQuestions = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const userId = req.params.id;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const query = `SELECT DISTINCT id,
                                   title,
                                   (SELECT username FROM users WHERE toUser = users.id) AS fromUser,
                                   (SELECT avatar FROM users WHERE toUser = users.id) AS avatar,
                                   askedDate,
                                   anonymous
                                   FROM questions WHERE fromUser = ? LIMIT ? OFFSET ?`;

    const args = [
        userId,
        count,
        offset
    ];

    database.query(query, args, (err, result) => {
        if (err) throw err;
        res.status(status.OK).json(result);
    });
};

exports.getQuestionByID = (req, res) => {
    const id = req.params.id;

    const query = `SELECT DISTINCT title,
                                   (SELECT username FROM users WHERE toUser = users.id) AS toUser,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUser,
                                   anonymous,
                                   askedDate
                                   FROM questions WHERE id = ? LIMIT 1`;

    database.query(query, id, (err, result) => {
        if (err) throw err;
        if (result.length == 1) {
            res.status(status.OK).json(result[0]);
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Invalid ID"
            });
        }
    });
};

exports.createNewQuestion = (req, res) => {
    const title = req.body.title;
    const toUser = req.body.toUser;
    const fromUser = req.body.fromUser;
    const anonymous = req.body.anonymous;

    const query = `INSERT INTO questions (title, toUser, fromUser, anonymous) VALUES (?, ?, ?, ?)`;

    const args = [
        title,
        toUser,
        fromUser,
        anonymous
    ];

    database.query(query, args, (err, result) => {
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Question created",
            });
        }
        else {
            res.status(status.BAD_REQUEST).json({
                message: "Question not created",
            });
        }
    });
};

exports.deleteQuestion = (req, res) => {
    const id = req.params.id;

    const query = `DELETE FROM questions WHERE id = ?`;

    database.query(query, id, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Question deleted",
            });
        }
        else {
            res.status(status.BAD_REQUEST).json({
                message: "Question not deleted",
            });
        }
    });
};
