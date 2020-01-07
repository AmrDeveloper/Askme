const status = require('../../utilities/server_status');
const answerModel = require('../models/answer');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getAnswerByID = (req, res) => {
    const id = req.params.id;
    answerModel.getAnswerByID(id).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.createNewAnswer = (req, res) => {
    const body = req.body.body;
    const questionId = req.body.questionId;
    const toUser = req.body.toUser;
    const fromUser = req.body.fromUser;
    const currentDate = Date.now();

    const args = [
        body,
        questionId,
        toUser,
        fromUser,
        currentDate
    ];

    answerModel.createNewAnswer(args).then(result => {
        if (result[0]) {

            const answerId = result[1];
            notificationModel.creatAnswerNotification(toUser, answerId);

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

    answerModel.deleteAnswer(id).then(state => {
        if (state0) {
            res.status(status.OK).json({
                message: "answer is deleted",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Delete answer"
            });
        }
    });
};