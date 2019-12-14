const status = require('../../utilities/server_status');
const questionModel = require('../models/question');
const notificationModel = require('../models/notification');

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

    const args = [userId, parseInt(count), parseInt(offset)];

    questionModel.getUserQuestions(args).then(result => {
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

    const args = [userId, parseInt(count), parseInt(offset)];

    questionModel.getAskedQuestions(args).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.getQuestionByID = (req, res) => {
    const id = req.params.id;

    questionModel.getQuestionByID(id).then(state => {
        if (state) {
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
    const currentDate = new Date().toISOString();

    const args = [
        title,
        toUser,
        fromUser,
        anonymous,
        currentDate
    ];

    questionModel.createNewQuestion(args).then(result => {
        if (result[0]) {
            const questionId = result[1];
            notificationModel.createQuestionNotification(toUser, questionId);
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

    questionModel.deleteQuestion(id).then(state => {
        if (state) {
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