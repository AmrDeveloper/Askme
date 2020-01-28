const database = require('../../database/config');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

exports.login = (email, password) => new Promise((resolve, reject) => {
    const sqlQuery = `SELECT id, color, password FROM users WHERE email = ? LIMIT 1`;

    database.query(sqlQuery, email, ((err, result) => {
        if (err) throw err;
        const validInfo = (result.length == 1);
        if (validInfo) {
            const encryptedPass = result[0]['password'];
            bcrypt.compare(password, encryptedPass, (err, isSame) => {
                if (err) throw err;
                if (isSame) {
                    jwt.sign(email, process.env.JWT_KEY, (err, token) => {
                        if (err) throw err;
                        resolve([true, {
                            "id": result[0]['id'],
                            "color" : result[0]['color'],
                            "token": token
                        }]);
                    });
                } else {
                    resolve([false]);
                }
            });
        } else {
            resolve([false]);
        }
    }));
});

exports.register = user => new Promise((resolve, reject) => {
    const query = "INSERT INTO users(name, email, username , password, joinDate, color) VALUES (?, ? , ? , ?, ?, ?)";
    database.query(query, user, (err, result) => {
        if (err) {
            if (err.code === 'ER_DUP_ENTRY') {
                resolve([false]);
            } else {
                throw err;
            }
        }else{
            if(result['affectedRows'] == 1){
                const email = user[1];
                jwt.sign(email, process.env.JWT_KEY, (err, token) => {
                    if (err) throw err;
                    resolve([true, {
                        "id": result.insertId,
                        "color" : "ORANGE",
                        "token": token
                    }]);
                });
            }else{
                resolve([false]);
            }
        }
    });
});

exports.queryUsers = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT users.id,
                                   users.name, 
                                   users.username,
                                   users.email,
                                   users.avatar,
                                   users.wallpaper,
                                   users.address,
                                   users.status,
                                   users.active,
                                   users.joinDate,
                                   users.color,
                                   (SELECT COUNT(*) FROM follows WHERE fromUser = users.id) AS following,
                                   (SELECT COUNT(*) FROM follows WHERE toUser = users.id) AS followers,
                                   (SELECT COUNT(*) FROM questions WHERE fromUser = users.id) AS questions,
                                   (SELECT COUNT(*) FROM answers WHERE fromUser = users.id) AS answers,
                                   (SELECT COUNT(*) FROM reactions WHERE toUser = users.id) AS likes,
                                   (SELECT IF(COUNT(*) >= 1, TRUE, FALSE) FROM follows WHERE fromUser = ? and toUser = users.id) AS isFollow
                  FROM users LIMIT ? OFFSET ?`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.getOneUser = args => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT users.id,
                                   users.name, 
                                   users.username,
                                   users.email,
                                   users.avatar,
                                   users.wallpaper,
                                   users.address,
                                   users.status,
                                   users.active,
                                   users.joinDate,
                                   users.color,
                                   (SELECT COUNT(*) FROM follows WHERE fromUser = users.id) AS following,
                                   (SELECT COUNT(*) FROM follows WHERE toUser = users.id) AS followers,
                                   (SELECT COUNT(*) FROM questions WHERE fromUser = users.id) AS questions,
                                   (SELECT COUNT(*) FROM answers WHERE fromUser = users.id) AS answers,
                                   (SELECT COUNT(*) FROM reactions WHERE toUser = users.id) AS likes,
                                   (SELECT IF(COUNT(*) >= 1, TRUE, FALSE) FROM follows WHERE fromUser = ? and toUser = users.id) AS isFollow
                  FROM users WHERE id = ? LIMIT 1`;

    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.getUserPassword = args => new Promise((resolve, reject) => {
    const sqlQuery = 'SELECT password FROM users WHERE id = ?';
    database.query(sqlQuery, args, (err, result) => {
        if (err) throw err;
        if (result.length == 1) {
            const oldPassword = result[0]['password'];
            resolve([true, oldPassword]);
        } else {
            resolve([false]);
        }
    });
});

exports.getUserAvatar = args => new Promise((resolve, reject) => {
    const selectQuery = "SELECT avatar FROM users WHERE email = ?";
    database.query(selectQuery, args, (err, result) => {
        if (err) throw err;
        const oldAvatar = result[0]['avatar'];
        resolve(oldAvatar);
    });
});

exports.getUserWallpaper = args => new Promise((resolve, reject) => {
    const selectQuery = "SELECT wallpaper FROM users WHERE email = ?";
    database.query(selectQuery, args, (err, result) => {
        if (err) throw err;
        const oldWallpaper = result[0]['wallpaper'];
        resolve(oldWallpaper);
    });
});

exports.searchUsers = (args) => new Promise((resolve, reject) => {
    const query = `SELECT DISTINCT users.id,
                                   users.name, 
                                   users.username,
                                   users.email,
                                   users.avatar,
                                   users.wallpaper,
                                   users.address,
                                   users.status,
                                   users.active,
                                   users.joinDate,
                                   users.color,
                                   (SELECT COUNT(*) FROM follows WHERE fromUser = users.id) AS following,
                                   (SELECT COUNT(*) FROM follows WHERE toUser = users.id) AS followers,
                                   (SELECT COUNT(*) FROM questions WHERE fromUser = users.id) AS questions,
                                   (SELECT COUNT(*) FROM answers WHERE fromUser = users.id) AS answers,
                                   (SELECT COUNT(*) FROM reactions WHERE toUser = users.id) AS likes
                  FROM users WHERE
                                name LIKE ? OR
                                username LIKE ? OR
                                email LIKE ? 
                                LIMIT ? OFFSET ?`;
    database.query(query, args, (err, result) => {
        if (err) throw err;
        resolve(result);
    });
});

exports.deleteUsers = () => new Promise((resolve, reject) => {
    const query = "DELETE * FROM users";
    database.query(query, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] > 0) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.deleteOneUser = args => new Promise((resolve, reject) => {
    const query = "DELETE FROM users WHERE email = ?";
    database.query(query, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] > 0) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.deleteUserStatus = args => new Promise((resolve, reject) => {
    const deleteQuery = 'UPDATE users SET status = "" WHERE email = ?';
    database.query(deleteQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.deleteUserAvatar = args => new Promise((resolve, reject) => {
    const deleteQuery = 'UPDATE users SET avatar = "" WHERE email = ?';
    database.query(deleteQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.deleteUserWallpaper = args => new Promise((resolve, reject) => {
    const deleteQuery = 'UPDATE users SET wallpaper = "" WHERE email = ?';
    database.query(deleteQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updateName = args => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET name = ? WHERE email = ?';

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updateUsername = args => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET username = ? WHERE email = ?';

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updateEmail = args => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET email = ? WHERE email = ?';

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updatePassword = args => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET password = ? WHERE id = ?';

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    })
});

exports.updateAddress = args => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET address = ? WHERE id = ?';

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updateStatus = args => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET status = ? WHERE id = ?';

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updateActive = args => new Promise((resolve, reject) => {
    const updateQuery = 'UPDATE users SET active = ? WHERE email = ?';

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updateUserAvatar = args => new Promise((resolve, reject) => {
    const updateQuery = "UPDATE users SET avatar = ? WHERE email = ?";

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updateUserWallpaper = args => new Promise((resolve, reject) => {
    const updateQuery = "UPDATE users SET wallpaper = ? WHERE email = ?";

    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.updateUserColor = args => new Promise((resolve, reject) => {
    const updateQuery = "UPDATE users SET color = ? WHERE id = ?";
    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            resolve(true);
        } else {
            resolve(false);
        }
    });
});

exports.hashPassword = (password) => new Promise((resolve, reject) => {
    bcrypt.hash(password, 10, (error, hashedPassword) => {
        if (error) throw error;
        resolve(hashedPassword);
    });
});

exports.comparePassword = (newPassword, oldPassword) => new Promise((resolve, reject) => {
    bcrypt.compare(newPassword, oldPassword, (err, isSame) => {
        if (err) throw err;
        resolve(isSame);
    });
});

exports.isAvailableInfo = info => new Promise((resolve, reject) => {
    const sqlQuery = `SELECT * FROM users WHERE email = ? or username = ? LIMIT 1`;
    database.query(sqlQuery, info, (err, result) => {
        if (err) throw err;
        const isAvailable = result.length == 0;
        resolve([isAvailable, result]);
    });
});