const express = require('express');
const controller = require('../controllers/user');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();
const multer = require('multer');

const avatarStorage = multer.diskStorage({
    destination: (request, file, callback) => {
        callback(null, 'storage/avatar/');
    },
    filename: (request, file, callback) => {
        const avatarName = Date.now() + file.originalname;
        callback(null, avatarName);
    }
});

const wallpaperStorage = multer.diskStorage({
    destination: (request, file, callback) => {
        callback(null, 'storage/wallpaper/');
    },
    filename: (request, file, callback) => {
        const avatarName = Date.now() + file.originalname;
        callback(null, avatarName);
    }
});

const fileFilter = (req, file, callback) => {
    if (file.mimetype === 'image/jpeg' || file.mimetype === 'image/png') {
        callback(null, true);
    } else {
        callback(null, false);
    }
};

const uploadAvatar = multer({
    storage: avatarStorage,
    fileFilter: fileFilter,
    limits: { fileSize: 1024 * 1024 * 5 }
});

const uploadWallpaper = multer({
    storage: wallpaperStorage,
    fileFilter: fileFilter,
    limits: { fileSize: 1024 * 1024 * 5 }
});

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

router.put('/avatar', uploadAvatar.single('avatar'), checkAuth, controller.updateUserAvatar);

router.put('/wallpaper', uploadWallpaper.single('wallpaper'), checkAuth, controller.updateUserWallpaper);

module.exports = router;