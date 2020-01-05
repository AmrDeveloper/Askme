const express = require('express');
const controller = require('../controllers/react');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.get('/', controller.getPostReactions)

router.post('/react', controller.createNewReaction);

router.post('/unreact', controller.deleteReaction);

module.exports = router;