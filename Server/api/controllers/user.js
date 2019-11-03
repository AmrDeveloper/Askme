const database = require('../../database/config');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const status = require('../../utilities/server_status');
const fileSystem = require('fs');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getAllUsers = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const query = `SELECT DISTINCT users.name, 
                                   users.username,
                                   users.email,
                                   users.email,
                                   users.avatar,
                                   users.address,
                                   users.status,
                                   users.active,
                                   users.joinDate,
                                   (SELECT COUNT(*) FROM follows WHERE fromUser = users.id) AS following,
                                   (SELECT COUNT(*) FROM follows WHERE toUser = users.id) AS followers,
                                   (SELECT COUNT(*) FROM questions WHERE fromUser = users.id) AS questions,
                                   (SELECT COUNT(*) FROM answers WHERE fromUser = users.id) AS answers,
                                   (SELECT COUNT(*) FROM reactions WHERE fromUser = users.id) AS likes
                  FROM users LIMIT ? OFFSET ?`;
    const args = [
        count,
        offset,
    ];
    database.query(query, args, (err, result) => {
        if (err) throw err;
        res.status(200).json(result);
    });
};

exports.getOneUser = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const email = req.params.id;
    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const query = `SELECT DISTINCT users.name, 
                                   users.username,
                                   users.email,
                                   users.email,
                                   users.avatar,
                                   users.address,
                                   users.status,
                                   users.active,
                                   users.joinDate,
                                   (SELECT COUNT(*) FROM follows WHERE fromUser = users.id) AS following,
                                   (SELECT COUNT(*) FROM follows WHERE toUser = users.id) AS followers,
                                   (SELECT COUNT(*) FROM questions WHERE fromUser = users.id) AS questions,
                                   (SELECT COUNT(*) FROM answers WHERE fromUser = users.id) AS answers,
                                   (SELECT COUNT(*) FROM reactions WHERE fromUser = users.id) AS likes
                  FROM users WHERE email = ? LIMIT ? OFFSET ?`;
    const args = [
        email,
        count,
        offset
    ];
    database.query(query, args, (err, result) => {
        if (err) throw err;
        res.status(200).json(result);
    });
};

exports.userLogin = (req, res) => {
    const email = req.body.email;
    const password = req.body.password;
    const sqlQuery = `SELECT password FROM users WHERE email = ? LIMIT 1`;
    database.query(sqlQuery, email, ((err, result) => {
        if (err) throw err;
        if (result.length == 1) {
            const encryptedPass = result[0]['password'];
            bcrypt.compare(password, encryptedPass, (err, isSame) => {
                if (err) throw err;
                if (isSame) {
                    jwt.sign(email, process.env.JWT_KEY, (err, token) => {
                        if (err) throw err;
                        res.status(200).json({
                            message: "Valid Login",
                            token: token
                        });
                    });
                } else {
                    res.status(401).json({
                        message: "Invalid Login",
                    });
                }
            });
        } else {
            res.status(401).json({
                message: "Invalid Login",
            });
        }
    }));
};

exports.registerNewUser = (req, res) => {
    const name = req.body.name;
    const email = req.body.email;
    const username = req.body.username;
    const password = req.body.password;
    const sqlQuery = `SELECT * FROM users WHERE email = ? or username = ? LIMIT 1`;
    const info = [
        email,
        username
    ];
    database.query(sqlQuery, info, (err, result) => {
        if (err) throw err;
        const isValidMail = result.length == 0;
        if (isValidMail) {
            bcrypt.hash(password, 10, (error, hashedPassword) => {
                if (error) {
                    return res.status(500).json({
                        error: error
                    })
                } else {

                    const query = "INSERT INTO users(name, email, username , password) VALUES (?, ? , ? , ?)";
                    const queryInfo = [
                        name,
                        email,
                        username,
                        hashedPassword
                    ];
                    database.query(query, queryInfo, (err, result) => {
                        if (err) throw err;
                        if (result['affectedRows'] == 1) {
                            res.status(200).json({
                                message: "Valid Register",
                            });
                        }
                        else {
                            res.status(401).json({
                                message: "Invalid Register",
                            });
                        }
                    });
                }
            });
        } else {
            if (result[0]['email'] == email) {
                return res.status(401).json({
                    message: "Invalid Email"
                })
            } else {
                return res.status(401).json({
                    message: "Invalid username"
                })
            }
        }
    });
};

exports.deleteAllUsers = (req, res) => {
    const query = "DELETE * FROM users";
    database.query(query, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] > 0) {
            res.status(status.OK).json({
                message: "All is deleted",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Delete All"
            });
        }
    });
};

exports.deleteOneUser = (req, res) => {
    const email = req.body.email;
    const query = "DELETE FROM users WHERE email = ?";
    database.query(query, email, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] > 0) {
            res.status(status.OK).json({
                message: "User is deleted",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Delete User"
            });
        }
    });
};

exports.deleteUserAvatar = (req, res) => {
    const email = req.body.email;
    const updateQuery = "SELECT avatar FROM users WHERE email = ?";
    database.query(updateQuery, email, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            const oldAvatar = result[0]['avatar'];
            if (oldAvatar != undefined || oldAvatar !== "null") {
                try {
                    fileSystem.unlinkSync(oldAvatar);
                } catch (err) {
                    console.error("Can't find file in storage/pictures Path");
                }
            }
            const deleteQuery = 'UPDATE users SET avatar = "" WHERE email = ?';
            database.query(deleteQuery, email, (err, result) => {
                if (err) throw err;
                if (result['affectedRows'] == 1) {
                    res.status(status.OK).json({
                        message: "Avatar Deleted",
                    });
                } else {
                    res.status(status.BAD_REQUEST).json({
                        message: "Can't Delete Avatar"
                    });
                }
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Delete Avatar"
            });
        }
    });
};

exports.deleteUserStatus = (req, res) => {
    const email = req.body.email;
    const deleteQuery = 'UPDATE users SET status = "" WHERE email = ?';
    database.query(deleteQuery, email, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Status Deleted",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Delete Status"
            });
        }
    });
};

exports.updateName = (req, res) => {
    const email = req.body.email;
    const name = req.body.name;
    const updateQuery = 'UPDATE users SET name = ? WHERE email = ?';
    const args = [
        name,
        email
    ];
    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Name changed",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't update Name"
            });
        }
    });
};

exports.updateUsername = (req, res) => {
    const email = req.body.email;
    const username = req.body.username;
    const updateQuery = 'UPDATE users SET username = ? WHERE email = ?';
    const args = [
        username,
        email
    ];
    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Username changed",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't update Username"
            });
        }
    });
};

exports.updateEmail = (req, res) => {
    const email = req.body.email;
    const newMail = req.body.newMail;
    const updateQuery = 'UPDATE users SET email = ? WHERE email = ?';
    const args = [
        newMail,
        email
    ];
    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Email changed",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't update Email"
            });
        }
    });
};

exports.updatePassword = (req, res) => {
    const email = req.body.email;
    const password = req.body.password;
    const sqlQuery = 'SELECT password FROM users WHERE email = ?';
    database.query(sqlQuery, email, (err, result) => {
        if (err) throw err;
        if (result.length == 1) {
            const oldPassword = result[0]['password'];
            bcrypt.compare(password, oldPassword, (err, isSame) => {
                if (err) throw err;
                if (isSame) {
                    res.status(status.BAD_REQUEST).json({
                        message: "New password is equal old one"
                    });
                } else {
                    bcrypt.hash(password, 10, (err, hash) => {
                        if (err) throw err;
                        const updateQuery = 'UPDATE users SET password = ? WHERE email = ?';
                        const args = [
                            hash,
                            email
                        ];
                        database.query(updateQuery, args, (err, result) => {
                            if (err) throw err;
                            if (result['affectedRows'] == 1) {
                                res.status(status.OK).json({
                                    message: "Password changed",
                                });
                            }
                        })
                    });
                }
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Invalid Information"
            });
        }
    });
};

exports.updateAddress = (req, res) => {
    const email = req.body.email;
    const address = req.body.address;
    const updateQuery = 'UPDATE users SET address = ? WHERE email = ?';
    const args = [
        address,
        email
    ];
    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Address changed",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't update Address"
            });
        }
    });
}

exports.updateStatus = (req, res) => {
    const email = req.body.email;
    const status = req.body.status;
    const updateQuery = 'UPDATE status FROM users SET status = ? WHERE email = ?';
    const args = [
        status,
        email
    ];
    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Status updated",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't update status"
            });
        }
    });
};

exports.updateActive = (req, res) => {
    const email = req.body.email;
    const active = req.body.active;
    const updateQuery = 'UPDATE users SET active = ? WHERE email = ?';
    const args = [
        active,
        email
    ];
    database.query(updateQuery, args, (err, result) => {
        if (err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Active changed",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't update Active"
            });
        }
    });
};

exports.updateUserAvatar = (req, res) => {
    const file = req.file;
    if (file == undefined) {
        res.status(status.BAD_REQUEST).json({
            message: "Can't upload iamge"
        });
    } else {
        const avatarPath = file.path;
        const email = req.body.email;
        const updateQuery = "SELECT avatar FROM users WHERE email = ?";
        database.query(updateQuery, email, (err, result) => {
            if (err) throw err;
            const oldAvatar = result[0]['avatar'];
            if (oldAvatar != undefined || oldAvatar !== "null") {
                try {
                    fileSystem.unlinkSync(oldAvatar);
                } catch (err) {
                    console.error("Can't find file in storage/pictures Path");
                }
            }
            const updateQuery = "UPDATE users SET avatar = ? WHERE email = ?";
            const args = [
                avatarPath,
                email
            ];
            database.query(updateQuery, args, (err, result) => {
                if (err) throw err;
                if (result['affectedRows'] == 1) {
                    res.status(status.OK).json({
                        message: "Avatar Updated",
                    });
                } else {
                    res.status(status.BAD_REQUEST).json({
                        message: "Can't update Avatar"
                    });
                }
            });
        });
    }
}