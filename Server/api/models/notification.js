const database = require('../../database/config');
const dateUtils = require('../../utilities/date_utils');

exports.getUserNotifications = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT 
                            notifications.id,
                            notifications.body,
                            notifications.createdDate,
                            notifications.action,
                            notifications.data,
                            notifications.opened
                            FROM notifications
                            WHERE toUser = ? 
                            ORDER BY createdDate DESC 
                            LIMIT ? OFFSET ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.getNotificationByID = args => new Promise((resolve, reject) => {
    const query = 'SELECT * FROM notifications WHERE id = ? LIMIT 1';
    database.query(query, args, (err, result) => {
        if (err) throw err;
        if (result.length == 1) {
            resolve([true, result[0]]);
        } else {
            resolve([false]);
        }
    });
});

exports.getNewNotifications = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT 
                        notifications.id,
                        notifications.body,
                        notifications.createdDate,
                        notifications.action,
                        notifications.data,
                        notifications.opened
                        FROM notifications 
                        WHERE toUser = ? AND opened = 0 
                        ORDER BY createdDate DESC 
                        LIMIT ? OFFSET ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.createNewNotification = args => new Promise((resolve, reject) => {
    const query = 'INSERT INTO notifications(toUser, body, action, opened, createdDate, data) VALUES(?, ?, ?, ?, ?, ?)';

    database.query(query, args, (err, result) => {
        if (err) {
            if (err.code === 'ER_DUP_ENTRY') {
                resolve(false);
            } else {
                throw err;
            }
        } else {
            const isValidCreation = (result['affectedRows'] == 1);
            resolve(isValidCreation);
        }
    });
});

exports.createFollowNotification = (toUser, fromUser) => new Promise((resolve, reject) => {
    const args = [
        toUser,
        "One user start following you",
        "follow",
        0,
        dateUtils.currentDate(),
        fromUser.toString()
    ];
    this.createNewNotification(args).then(state => resolve(state));
});

exports.createQuestionNotification = (toUser, questionId) => new Promise((resolve, reject) => {
    const args = [
        toUser,
        "You have new Question check it now",
        "question",
        0,
        dateUtils.currentDate(),
        questionId.toString()
    ];
    this.createNewNotification(args).then(state => resolve(state));
});

exports.creatAnswerNotification = (toUser, answerId) => new Promise((resolve, reject) => {
    const args = [
        toUser,
        "Someone answer your question check it now",
        "answer",
        0,
        dateUtils.currentDate(),
        answerId.toString()
    ];
    this.createNewNotification(args).then(state => resolve(state));
});

exports.deleteAllNotifications = () => new Promise((resolve, reject) => {
    const query = 'DELETE FROM notifications';
    database.query(query, (err, result) => {
        if (err) throw err;
        const isValidDelete = result['affectedRows'] == 1;
        resolve(isValidDelete);
    });
});

exports.deleteNotificationByID = args => new Promise((resolve, reject) => {
    const query = 'DELETE FROM notifications WHERE id = ?';
    database.query(query, args, (err, result) => {
        if (err) throw err;
        const isValidDelete = result['affectedRows'] == 1;
        resolve(isValidDelete);
    });
});

exports.makeNotificationReaded = args => new Promise((resolve, reject) => {
    const query = 'UPDATE notifications SET opened = 1 WHERE id = ?';
    database.query(query, args, (err, result) => {
        if (err) throw err;
        const isValidUpdated = result['affectedRows'] == 1;
        resolve(isValidUpdated);
    });
});