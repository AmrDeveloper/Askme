const express = require('express');
const controller = require('../controllers/user');
const checkAuth = require('../../middleware/check_auth');
const multerController = require('../../utilities/multer_setup');
const router = express.Router();

router.get('/', controller.getAllUsers);

router.get('/search', controller.searchUsers);

router.get('/:id', controller.getOneUser);

router.post('/login', controller.userLogin);

router.post('/register', controller.registerNewUser);

router.delete('/all', controller.deleteAllUsers);

router.delete('/avatar', controller.deleteUserAvatar);

router.delete('/wallpaper', controller.deleteUserWallpaper);

router.delete('/status', checkAuth, controller.deleteUserStatus);

router.delete('/', checkAuth, controller.deleteOneUser);

router.put('/name', checkAuth, controller.updateName);

router.put('/username', checkAuth, controller.updateUsername);

router.put('/email', checkAuth, controller.updateEmail);

router.put('/password', checkAuth, controller.updatePassword);

router.put('/address', checkAuth, controller.updateAddress);

router.put('/status', controller.updateStatus);

router.put('/active', checkAuth, controller.updateActive);

router.put('/avatar', multerController.uploadAvatar.single('avatar'), controller.updateUserAvatar);

router.put('/wallpaper', multerController.uploadWallpaper.single('wallpaper'), checkAuth, controller.updateUserWallpaper);

router.put('/color', checkAuth, controller.updateUserColor);

module.exports = router;