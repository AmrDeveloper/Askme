const status = require('../../utilities/server_status');
const reactModel = require('../models/react');

const QUERY_DEFAULT_PAGE = 0;
const QUERY_DEFAULT_PAGE_SIZE = 25;
const QUERY_MAX_COUNT = 50;

exports.getPostReactions = (req, res) => {
    var page = req.query.page;
    var page_size = req.query.page_size;

    if (userId == null) {
        userId = 0;
    }

    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    const offset = page * page_size;

    const answerId = req.body.answerId;

    const args = [answerId, parseInt(page_size), parseInt(offset)];

    reactModel.getPostReactions(args).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.createNewReaction = (req, res) => {
    const fromUser = req.body.fromUser;
    var toUser = req.body.toUser;
    const answerId = req.body.answerId;
    var reactionsType = req.body.reactionsType;

    if(reactionsType == null){
        reactionsType = 0;
    }

    if(toUser == null){
        toUser = "";
    }

    const args = [fromUser, toUser, answerId, reactionsType];

    reactModel.createNewReaction(args).then(state => {
        if (state) {
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
    const fromUser = req.body.fromUser;
    const answerId = req.body.answerId;

    const args = [fromUser, answerId];

    reactModel.deleteReaction(args).then(state => {
        if (state) {
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