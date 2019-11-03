const database = require('../../database/config');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserFollowing = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    res.status(200).json({
        message: "Get all user following"
    })
};

exports.getUserFollowers = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    res.status(200).json({
        message: "Get all user followers"
    })
};

exports.followUser = (req, res) => {
    res.status(200).json({
        message: "follow user"
    })
};

exports.unFollowUser = (req, res) => {
    res.status(200).json({
        message: "un follow user"
    })
};