const express = require('express');
const controller = require('../controllers/feed');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.get('/', checkAuth, controller.getUserFeed);

module.exports = router;