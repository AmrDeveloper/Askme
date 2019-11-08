const express = require('express');
const controller = require('../controllers/react');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.get('/', checkAuth, controller.getPostReactions)

router.post('/', checkAuth, controller.createNewReaction);

router.delete('/', checkAuth, controller.deleteReaction);

module.exports = router;