const database = require('../../database/config');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const status = require('../../utilities/server_status');

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

    res.status(200).json({
        message: "Get All users"
    });
};

exports.getOneUser = (req, res) => {
    res.status(200).json({
        message: "GET One user by id"
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
    res.status(200).json({
        message: "Delete All users"
    });
};

exports.deleteOneUser = (req, res) => {
    res.status(200).json({
        message: "Delete One user by id"
    });
};

exports.deleteUserAvatar = (req, res) => {
    res.status(200).json({
        message: "Delete User Avatar"
    });
};

exports.deleteUserStatus = (req, res) => {
    res.status(200).json({
        message: "Delete User Status"
    });
};

exports.updateName = (req, res) => {
    res.status(200).json({
        message: "Update name"
    });
};

exports.updateUsername = (req, res) => {
    res.status(200).json({
        message: "Update username"
    });
};

exports.updateEmail = (req, res) => {
    res.status(200).json({
        message: "Update user email"
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
    res.status(200).json({
        message: "Update user Address"
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
        if(err) throw err;
        if (result['affectedRows'] == 1) {
            res.status(status.OK).json({
                message: "Status updated",
            });
        }else{
            res.status(status.BAD_REQUEST).json({
                message: "Can't update status"
            });
        }
    });
};

exports.updateActive = (req, res) => {
    res.status(200).json({
        message: "Update user Active"
    });
};

exports.updateUserAvatar = (req, res) => {
    res.status(200).json({
        message: "Update user Avatar"
    });
}