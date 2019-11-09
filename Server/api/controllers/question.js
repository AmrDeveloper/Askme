const database = require('../../database/config');
const status = require('../../utilities/server_status');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserQuestions = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    res.status(200).json({
        message: "Get all questions come to user"
    })
};

exports.getAskedQuestions = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    res.status(200).json({
        message: "Get all question sended by user"
    });
};

exports.getQuestionByID = (req, res) => {
    res.status(200).json({
        message: "Get Question by ID"
    });
};

exports.createNewQuestion = (req, res) => {
    res.status(200).json({
        message: "Create new Question"
    });
};

exports.deleteQuestion = (req, res) => {
    res.status(200).json({
        message: "Delete Question by ID"
    });
};
