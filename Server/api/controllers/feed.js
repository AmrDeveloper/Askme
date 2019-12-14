const status = require('../../utilities/server_status');
const feedModel = require('../models/feed');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;


exports.getUserFeed = (req, res) => {
    const email = req.query.id;
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const args = [email,count, offset]
    feedModel.getUserFeed(args).then(result => {
        res.status(status.OK).json(result);
    })
};