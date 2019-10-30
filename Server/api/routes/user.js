const express = require('express');
const controller = require('../controllers/user');
const router = express.Router();

router.get('/', controller.getAllUsers);

router.get('/:id', controller.getOneUser);

router.post('/', controller.registerNewUser);

router.delete('/', controller.deleteAllUsers);

router.delete('/:id', controller.deleteOneUser);

router.put('/name', controller.updateUsername);

router.put('/email', controller.updateUserEmail);

router.put('/password', controller.updateUserPassword);

module.exports = router;