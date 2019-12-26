const express = require('express');
const controller = require('../controllers/question');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.get('/:id', controller.getQuestionByID);

router.get('/:id/questions', controller.getUserQuestions);

router.get('/:id/asked', controller.getAskedQuestions);

router.post('/',  controller.createNewQuestion);

router.delete('/:id', controller.deleteQuestion);

module.exports = router;