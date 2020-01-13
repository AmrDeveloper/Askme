const status = require('../../utilities/server_status');
const notificationModel = require('../models/notification');
const dateUtils = require('../../utilities/date_utils');

const QUERY_DEFAULT_PAGE = 0;
const QUERY_DEFAULT_PAGE_SIZE = 25;
const QUERY_MAX_COUNT = 50;

exports.getAllNotifications = (req, res) => {
    const id = req.query.id;
    var page = req.query.page;
    var page_size = req.query.page_size;

    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    const offset = page * page_size;

    const args = [id, parseInt(page_size), parseInt(offset)];

    notificationModel.getUserNotifications(args)
        .then(result => {
            console.log(`result ${id}  : ${result.length}`)
            res.status(status.OK).json(result);
        });
};

exports.getNotificationByID = (req, res) => {
    const notificationID = req.params.id;

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
    const id = req.params.id;
    var page = req.query.page;
    var page_size = req.query.page_size;
    
    if (page == null) {
        page = QUERY_DEFAULT_PAGE;
    }

    if (page_size == null || page_size > QUERY_MAX_COUNT) {
        page_size = QUERY_DEFAULT_PAGE_SIZE;
    }

    const offset = page * page_size;

    const args = [id, parseInt(page_size), parseInt(offset)];

    notificationModel.getNewNotifications(args).then(result => {
        res.status(status.OK).json(result);
    })
};

exports.createNewNotification = (req, rse) => {
    const toUser = req.body.toUser;
    const body = req.body.body;
    const action = req.body.action;
    const currentDate = dateUtils.currentDate();

    if(action == null){
        action = "normal";
    }

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
    const notificationID = req.params.id;

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
    const notificationID = req.params.id;
    
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