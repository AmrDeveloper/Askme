const express = require('express');
const controller = require('../controllers/follow');
const router = express.Router();

router.get('/:id/following', controller.getUserFollowing);

router.get('/:id/followers', controller.getUserFollowers);

router.post('/:id/follow', controller.followUser);

router.post('/:id/unfollow', controller.unFollowUser);

module.exports = router;