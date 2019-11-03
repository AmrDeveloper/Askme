const database = require('../../database/config');
const status = require('../../utilities/server_status');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getAllNotifications = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const id = req.body.id;
    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    console.log(id)
    const query = `SELECT DISTINCT 
                        notifications.id,
                        notifications.body,
                        notifications.createdDate,
                        notifications.action,
                        notifications.opened
                        FROM notifications WHERE toUser = ? LIMIT ? OFFSET ?`;
    const args = [
        id,
        count,
        offset
    ];

    database.query(query, args, (err, result) => {
        if (err) throw err;
        res.status(status.OK).json(result);
    })
};

exports.getNotificationByID = (req, res) => {
    const notificationID = req.body.id;
    const query = 'SELECT * FROM notifications WHERE id = ? LIMIT 1';
    database.query(query, notificationID, (err, result) => {
        if (err) throw err;
        if (result.length == 1) {
            res.status(status.OK).json(result[0]);
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
    const id = req.body.id;
    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const query = `SELECT DISTINCT 
                        notifications.id,
                        notifications.body,
                        notifications.createdDate,
                        notifications.action,
                        notifications.opened
                        FROM notifications WHERE toUser = ? AND opened = 0 LIMIT ? OFFSET ?`;
    const args = [
        id,
        count,
        offset
    ];

    database.query(query, args, (err, result) => {
        if (err) throw err;
        res.status(status.OK).json(result);
    });
};

exports.createNewNotification = (req, rse) => {
    const id = req.body.id;
    const body = req.body.body;
    const action = req.body.action;

    const query = 'INSERT INTO notifications(toUser, body, action, opened) VALUES(?, ?, ?, ?)';

    const args = [
        id,
        body,
        action,
        0
    ];

    database.query(query, args, (err, result) => {
        if(err) throw err;
        if (result['affectedRows'] == 1) {
            rse.status(status.OK).json({
                message: "Notification Created"
            })
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Created notification"
            })
        }
    });
};

exports.deleteAllNotifications = (req, res) => {
    const query = 'DELETE FROM notifications';
    database.query(query, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Notifications Deleted"
            })
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Deleted notifications"
            })
        }
    });
};

exports.deleteNotificationByID = (req, res) => {
    const notificationID = req.body.id;
    const query = 'DELETE FROM notifications WHERE id = ?';
    database.query(query, notificationID, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Notification Deleted"
            })
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Deleted notification with this id"
            })
        }
    });
};

exports.makeNotificationReaded = (req, res) => {
    const notificationID = req.body.id;
    const query = 'UPDATE notifications SET opened = 1 WHERE id = ?';
    database.query(query, notificationID, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Notification Readed"
            })
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't update notification with this id"
            })
        }
    });
};