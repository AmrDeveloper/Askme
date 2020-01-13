const status = require('../../utilities/server_status');
const answerModel = require('../models/answer');
const notificationModel = require('../models/notification');

exports.getAnswerByID = (req, res) => {
    const id = req.params.id;
    answerModel.getAnswerByID(id).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.getQuestionAnswer = (req, res) => {
    const id = req.params.id;
    const userId = req.query.userId;
    const args = [userId, id];
    answerModel.getQuestionAnswer(args).then(result => {
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