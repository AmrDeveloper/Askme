const status = require('../../utilities/server_status');
const notificationModel = require('../models/notification');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getAllNotifications = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const id = req.param.id;
    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const args = [id, count, offset];

    notificationModel.getUserNotifications(args)
        .then(result => {
            res.status(status.OK).json(result);
        });
};

exports.getNotificationByID = (req, res) => {
    const notificationID = req.param.id;

    notificationModel.getNotificationByID(notificationID)
        .then(result => {
            if (result[0]) {
                res.status(status.OK).json(result[1][0]);
            } else {
                res.status(status.BAD_REQUEST).json({
                    message: "Can't find notification with this id"
                });
            }
        });
};

exports.getUnReadedNotification = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const id = req.param.id;
    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const args = [id, count, offset];

    notificationModel.getNewNotifications(args).then(result => {
        res.status(status.OK).json(result);
    })
};

exports.createNewNotification = (req, rse) => {
    const toUser = req.body.toUser;
    const body = req.body.body;
    const action = req.body.action;
    const currentDate = new Date().toISOString();

    const args = [toUser, body, action, 0, currentDate, ""];

    notificationModel.createNewNotification(args)
        .then(state => {
            if (state) {
                rse.status(status.OK).json({
                    message: "Notification Created"
                }); 
            } else {
                res.status(status.BAD_REQUEST).json({
                    message: "Can't Created notification"
                });
            }
        });
};

exports.deleteAllNotifications = (req, res) => {
    notificationModel.deleteAllNotifications()
        .then(state => {
            if (state) {
                res.status(status.OK).json({
                    message: "Notifications Deleted"
                });
            } else {
                res.status(status.BAD_REQUEST).json({
                    message: "Can't Deleted notifications"
                });
            }
        });
};

exports.deleteNotificationByID = (req, res) => {
    const notificationID = req.param.id;

    notificationModel.deleteNotificationByID(notificationID)
        .then(state => {
            if (state) {
                res.status(status.OK).json({
                    message: "Notification Deleted"
                });
            } else {
                res.status(status.BAD_REQUEST).json({
                    message: "Can't Deleted notification with this id"
                });
            }
        });
};

exports.makeNotificationReaded = (req, res) => {
    const notificationID = req.param.id;

    notificationModel.makeNotificationReaded(notificationID)
        .then(state => {
            if (state) {
                res.status(status.OK).json({
                    message: "Notification Readed"
                });
            } else {
                res.status(status.BAD_REQUEST).json({
                    message: "Can't update notification with this id"
                });
            }
        });
};