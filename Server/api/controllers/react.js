const status = require('../../utilities/server_status');
const reactModel = require('../models/react');

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

    const answerId = req.body.answerId;

    const args = [answerId, parseInt(count), parseInt(offset)];

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