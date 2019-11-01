const express = require('express');
const controller = require('../controllers/user');
const router = express.Router();

router.get('/', controller.getAllUsers);

router.get('/:id', controller.getOneUser);

router.post('/login', controller.userLogin);

router.post('/register', controller.registerNewUser);

router.delete('/', controller.deleteAllUsers);

router.delete('/:id', controller.deleteOneUser);

router.put('/name', controller.updateName);

router.put('/username', controller.updateUsername);

router.put('/email', controller.updateEmail);

router.put('/password', controller.updatePassword);

router.put('/address', controller.updateAddress);

router.put('/status', controller.updateStatus)

router.put('/active', controller.updateActive)

module.exports = router;