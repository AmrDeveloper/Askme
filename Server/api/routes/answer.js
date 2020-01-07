const express = require('express');
const controller = require('../controllers/answer');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.get('/:id', controller.getAnswerByID);

router.get('/question/:id', controller.getQuestionAnswer);

router.post('/', controller.createNewAnswer);

router.delete('/:id', checkAuth, controller.deleteAnswer);

module.exports = router;