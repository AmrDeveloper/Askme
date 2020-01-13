const status = require('../../utilities/server_status');
const feedModel = require('../models/feed');

const QUERY_DEFAULT_PAGE = 0;
const QUERY_DEFAULT_PAGE_SIZE = 25;
const QUERY_MAX_COUNT = 50;

exports.getUserFeed = (req, res) => {
    const id = req.query.id;
    var page = req.query.page;
    var page_size = req.query.page_size;
    var userId = req.query.userId;

    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    if (userId == null) {
        userId = 0;
    }

    const offset = page * page_size;

    const args = [userId, id, parseInt(page_size), parseInt(offset)]
    feedModel.getUserFeed(args).then(result => {
        res.status(status.OK).json(result);
    })
};

exports.getHomeFeed = (req, res) => {
    const id = req.query.id;
    var page = req.query.page;
    var page_size = req.query.page_size;

    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    const offset = page * page_size;

    const args = [id, id, parseInt(page_size), parseInt(offset)]
    feedModel.getHomeFeed(args).then(result => {
        res.status(status.OK).json(result);
    })
};