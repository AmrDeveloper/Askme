const status = require('../../utilities/server_status');
const questionModel = require('../models/question');
const notificationModel = require('../models/notification');
const dateUtils = require('../../utilities/date_utils');

const QUERY_DEFAULT_PAGE = 0;
const QUERY_DEFAULT_PAGE_SIZE = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserQuestions = (req, res) => {
    const userId = req.params.id;
    var page = req.query.page;
    var page_size = req.query.page_size;

    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    const offset = page * page_size;
    
    const args = [userId, parseInt(page_size), parseInt(offset)];

    questionModel.getUserQuestions(args).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.getAskedQuestions = (req, res) => {
    const userId = req.params.id;
    var page = req.query.page;
    var page_size = req.query.page_size;

    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    const offset = page * page_size;

    const args = [userId, parseInt(page_size), parseInt(offset)];

    questionModel.getAskedQuestions(args).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.getQuestionByID = (req, res) => {
    const id = req.params.id;

    questionModel.getQuestionByID(id).then(result => {
        if (result) {
            res.status(status.OK).json(result[1]);
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
    const currentDate = dateUtils.currentDate();

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