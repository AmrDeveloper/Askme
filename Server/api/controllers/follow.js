const status = require('../../utilities/server_status');
const followModel = require('../models/follow');
const notificationModel = require('../models/notification');

const QUERY_DEFAULT_PAGE = 0;
const QUERY_DEFAULT_PAGE_SIZE = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserFollowing = (req, res) => {
    const id = req.body.id;
    var page = req.query.page;
    var page_size = req.query.page_size;

    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    const offset = page * page_size;

    const args = [id, parseInt(page_size), parseInt(offset)];

    followModel.getUserFollowing(args).then(result => {
        res.status(status.OK).json(result);
    });
};

exports.getUserFollowers = (req, res) => {
    const id = req.body.id;
    var page = req.query.page;
    var page_size = req.query.page_size;

    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    const offset = page * page_size;

    const args = [id, parseInt(page_size), parseInt(offset)];

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