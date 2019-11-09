const database = require('../../database/config');
const status = require('../../utilities/server_status');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getAnswerByID = (req, res) => {
    res.status(200).json({
        message: "Get Answer by id"
    })
};

exports.createNewAnswer = (req,res) => {
    res.status(200).json({
        message: "Post new Answer"
    })
};

exports.deleteAnswer = (req,res) => {
    res.status(200).json({
        message: "Delete answer by id"
    })
};