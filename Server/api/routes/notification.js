const express = require('express');
const controller = require('../controllers/notification');
const checkAuth = require('../../middleware/check_auth');
const router = express.Router();

router.get('/', checkAuth, controller.getAllNotifications);

router.get('/news', checkAuth, controller.getUnReadedNotification);

router.get('/:id', checkAuth, controller.getNotificationByID);

router.post('/', checkAuth, controller.createNewNotification);

router.delete('/', checkAuth, controller.deleteAllNotifications);

router.delete('/:id', checkAuth, controller.deleteNotificationByID);

router.put('/readed', checkAuth, controller.makeNotificationReaded);

module.exports = router;