const express = require('express');
const controller = require('../controllers/answer');
const router = express.Router();

router.get('/:id', controller.getAnswerByID);

router.post('/', controller.createNewAnswer);

router.delete('/:id', controller.deleteAnswer);

module.exports = router;