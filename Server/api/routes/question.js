const express = require('express');
const controller = require('../controllers/question');
const router = express.Router();

router.get('/:id/questions', controller.getUserQuestions);

router.get('/:id/asked', controller.getAskedQuestions);

router.get('/:id', controller.getQuestionByID);

router.post('/', controller.createNewQuestion);

router.delete('/:id', controller.deleteQuestion);

module.exports = router;