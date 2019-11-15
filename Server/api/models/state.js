const database = require('../../database/config');

exports.makeUserOnline = id => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET active = 1 WHERE id = ?';

    database.query(updateQuery, id, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.makeUserOffline = id => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET active = 0 WHERE id = ?';

    database.query(updateQuery, id, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});