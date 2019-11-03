const express = require('express');
const controller = require('../controllers/user');
const router = express.Router();
const multer = require('multer');

const storage = multer.diskStorage({
    destination: (request, file, callback) => {
        callback(null, 'storage/pictures/');
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

const upload = multer({
    storage: storage,
    fileFilter: fileFilter,
    limits: {fileSize: 1024 * 1024 * 5  }
});

router.get('/', controller.getAllUsers);

router.get('/:id', controller.getOneUser);

router.post('/login', controller.userLogin);

router.post('/register', controller.registerNewUser);

router.delete('/', controller.deleteAllUsers);

router.delete('/:id', controller.deleteOneUser);

router.delete('/avatar', controller.deleteUserAvatar);

router.delete('/status', controller.deleteUserStatus);

router.put('/name', controller.updateName);

router.put('/username', controller.updateUsername);

router.put('/email', controller.updateEmail);

router.put('/password', controller.updatePassword);

router.put('/address', controller.updateAddress);

router.put('/status', controller.updateStatus)

router.put('/active', controller.updateActive)

router.put('/avatar', upload.single('avatar'), controller.updateUserAvatar);

module.exports = router;