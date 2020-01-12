const status = require('../../utilities/server_status');
const feedModel = require('../models/feed');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;


exports.getUserFeed = (req, res) => {
    const id = req.query.id;
    var offset = req.query.offset;
    var count = req.query.count;
    var userId = req.query.userId;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    if (userId == null) {
        userId = 0;
    }

    offset = offset * count;

    const args = [userId, id, parseInt(count), parseInt(offset)]
    feedModel.getUserFeed(args).then(result => {
        console.log(`Feed ${id}  : ${result.length}`)
        res.status(status.OK).json(result);
    })
};

exports.getHomeFeed = (req, res) => {
    const id = req.query.id;
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    offset = offset * count;

    const args = [id, id, parseInt(count), parseInt(offset)]
    feedModel.getHomeFeed(args).then(result => {
        console.log(`Home ${id}  : ${result.length}`)
        res.status(status.OK).json(result);
    })
};