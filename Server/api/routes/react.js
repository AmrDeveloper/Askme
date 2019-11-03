const express = require('express');
const controller = require('../controllers/react');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.post('/', checkAuth, controller.createNewReact);

router.delete('/', checkAuth, controller.deleteReact);

module.exports = router;