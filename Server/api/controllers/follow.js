const status = require('../../utilities/server_status');
const followModel = require('../models/follow');
const notificationModel = require('../models/notification');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserFollowing = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const id = req.body.id;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    offset = offset * count;

    const args = [id, parseInt(count), parseInt(offset)];

    followModel.getUserFollowing(args).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.getUserFollowers = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const id = req.body.id;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    offset = offset * count;

    const args = [id, parseInt(count), parseInt(offset)];

    followModel.getUserFollowers(args).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.followUser = (req, res) => {
    const fromUser = req.body.fromUser;
    const toUser = req.body.toUser;

    const args = [fromUser, toUser];

    followModel.followUser(args).then(state => {
        if (state) {
            notificationModel.createFollowNotification(toUser, fromUser);
            res.status(status.OK).json({
                message: "Follow is done",
            }); 
        }
        else {
            res.status(status.BAD_REQUEST).json({
                message: "Invalid Follow",
            });
        }
    });
};

exports.unFollowUser = (req, res) => {
    const fromUser = req.body.fromUser;
    const toUser = req.body.toUser;

    const args = [fromUser, toUser];

    followModel.unfollowUser(args).then(state => {
        if (state) {
            res.status(status.OK).json({
                message: "Un Follow is done",
            });
        }
        else {
            res.status(status.BAD_REQUEST).json({
                message: "Invalid un Follow",
            });
        }
    });
};