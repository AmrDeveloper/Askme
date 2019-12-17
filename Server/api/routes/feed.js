const express = require('express');
const controller = require('../controllers/feed');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.get('/', controller.getUserFeed);

router.get('/home', controller.getHomeFeed);

module.exports = router;