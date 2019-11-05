const express = require('express');
const controller = require('../controllers/follow');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.get('/following', controller.getUserFollowing);

router.get('/followers', controller.getUserFollowers);

router.post('/follow', checkAuth, controller.followUser);

router.post('/unfollow', checkAuth, controller.unFollowUser);

module.exports = router;