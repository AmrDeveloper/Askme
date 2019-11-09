const database = require('../../database/config');
const status = require('../../utilities/server_status');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getAnswerByID = (req, res) => {
    const id = req.params.id;

    const query = `SELECT DISTINCT id,
                                   body,
                                   (SELECT username FROM users WHERE fromUser = users.id) AS fromUser,
                                   (SELECT avatar FROM users WHERE fromUser = users.id) AS avatar,
                                   answerdDate
                                   FROM answers WHERE questionId = ?`;

    database.query(query, id, (err, result) => {
        if (err) throw err;
        res.status(status.OK).json(result);
    });
};

exports.createNewAnswer = (req, res) => {
    const body = req.body.body;
    const questionId = req.body.questionId;
    const toUser = req.body.toUser;
    const fromUser = req.body.fromUser;

    const query = 'INSERT INTO answers(body, questionId, toUser, fromUser) VALUES (?, ?, ?, ?)';

    const args = [
        body,
        questionId,
        toUser,
        fromUser
    ];

    database.query(query, args, (err, result) => {
       if(err) throw err;
       if (result['affectedRows'] > 0) {
        res.status(status.OK).json({
            message: "answer is created",
        });
    } else {
        res.status(status.BAD_REQUEST).json({
            message: "Can't create answer"
        });
    }
    });
};

exports.deleteAnswer = (req, res) => {
    const id = req.params.id;

    const query = 'DELETE FROM answers WHERE id = ?';

    database.query(query, id, (err, result => {
        if (err) throw err;
        if (result['affectedRows'] > 0) {
            res.status(status.OK).json({
                message: "answer is deleted",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Delete answer"
            });
        }
    }));
};