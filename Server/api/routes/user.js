const express = require('express');
const controller = require('../controllers/user');
const checkAuth = require('../../middleware/check_auth');
const multerController = require('../../utilities/multer_setup');
const router = express.Router();

router.get('/', controller.getAllUsers);

router.get('/:username', controller.getOneUser);

router.post('/login', controller.userLogin);

router.post('/register', controller.registerNewUser);

router.delete('/', controller.deleteAllUsers);

router.delete('/avatar', controller.deleteUserAvatar);

router.delete('/wallpaper', controller.deleteUserWallpaper);

router.delete('/status', checkAuth, controller.deleteUserStatus);

router.delete('/:id', checkAuth, controller.deleteOneUser);

router.put('/name', checkAuth, controller.updateName);

router.put('/username', checkAuth, controller.updateUsername);

router.put('/email', checkAuth, controller.updateEmail);

router.put('/password', checkAuth, controller.updatePassword);

router.put('/address', checkAuth, controller.updateAddress);

router.put('/status', checkAuth, controller.updateStatus);

router.put('/active', checkAuth, controller.updateActive);

router.put('/avatar', multerController.single('avatar'), checkAuth, controller.updateUserAvatar);

router.put('/wallpaper', multerController.single('wallpaper'), checkAuth, controller.updateUserWallpaper);

router.put('/color', checkAuth, controller.updateUserColor);

module.exports = router;