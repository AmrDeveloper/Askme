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
        coumt,
        offset
    ];
    database.query(sqlQuery, args, (err, result) => {
        if (err) throw err;
        res.status(status.OK).json(result);
    });
};

exports.createNewReact = (req, res) => {
     
};

exports.deleteReact = (req, res) => {

};