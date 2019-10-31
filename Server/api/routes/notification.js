const express = require('express');
const controller = require('../controllers/notification');
const router = express.Router();

router.get('/', controller.getAllNotifications);

router.get('/:id', controller.getNotificationByID);

router.post('/', controller.createNewNotification);

router.delete('/', controller.deleteAllNotifications);

router.delete('/:id', controller.deleteNotificationByID);

module.exports = router;