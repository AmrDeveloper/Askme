const status = require('../../utilities/server_status');
const userModel = require('../models/user');
const fileSystem = require('fs');

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.userLogin = (req, res) => {
    const email = req.body.email;
    const password = req.body.password;

    userModel.login(email, password).then((result) => {
        if (result[0]) {
            res.status(status.OK).json({
                message: "Valid Login",
                token: result[1]
            });
        } else {
            res.status(statis.BAD_REQUEST).json({
                message: "Invalid Login",
            });
        }
    })
};

exports.registerNewUser = (req, res) => {
    const name = req.body.name;
    const email = req.body.email;
    const username = req.body.username;
    const password = req.body.password;
    const currentDate = new Date().toISOString();

    const info = [
        email,
        username,
        currentDate
    ];

    userModel.isAvailableInfo(info).then(result => {
        const isAvailable = result[0];
        if (isAvailable) {
            userModel.hashPassword(password).then(hashedPassword => {
                const user = [
                    name,
                    email,
                    username,
                    hashedPassword
                ];

                userModel.register(user).then(state => {
                    if (state) {
                        res.status(status.OK).json({
                            message: "Valid Register",
                        });
                    } else {
                        res.status(status.BAD_REQUEST).json({
                            message: "Invalid Register",
                        });
                    }
                });
            });
        } else {
            const current = result[1][0]['email'];
            if (current == email) {
                return res.status(status.BAD_REQUEST).json({
                    message: "Invalid Email"
                })
            } else {
                return res.status(status.BAD_REQUEST).json({
                    message: "Invalid username"
                })
            }
        }
    });
};

exports.getAllUsers = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;

    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const args = [count, offset];

    userModel.queryUsers(args).then(result => { res.status(status.OK).json(result) });
};

exports.getOneUser = (req, res) => {
    var offset = req.query.offset;
    var count = req.query.count;
    const username = req.params.username;
    if (offset == null) {
        offset = QUERY_DEFAULT_OFFSET;
    }

    if (count == null || count > QUERY_MAX_COUNT) {
        count = QUERY_DEFAULT_COUNT;
    }

    const args = [username, count, offset];
    console.log("username")
    userModel.getOneUser(args).then(result => { res.status(status.OK).json(result); })
};

exports.deleteAllUsers = (req, res) => {
    userModel.deleteUsers().then(state => {
        if (state) {
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
    userModel.deleteOneUser(email).then(state => {
        if (state) {
            res.status(status.OK).json({
                message: "User is deleted",
            });
        } else {
            res.status(status.BAD_REQUEST).json({
                message: "Can't Delete User"
            });
        }
    })
};

exports.deleteUserAvatar = (req, res) => {
    const email = req.body.email;

    userModel.getUserAvatar(email).then(oldAvatar => {
        if (oldAvatar != undefined || oldAvatar !== "null") {
            try {
                fileSystem.unlinkSync(oldAvatar);
            } catch (err) {
                console.error("Can't find file in storage/pictures Path");
            }
        }
        userModel.deleteUserAvatar(email).then(state => {
            if (state) {
                res.status(status.OK).json({
                    message: "Avatar Deleted",
                });
            } else {
                res.status(status.BAD_REQUEST).json({
                    message: "Can't Delete Avatar 1"
                });
            }
        })

    });
};

exports.deleteUserStatus = (req, res) => {
    const email = req.body.email;

    userModel.deleteUserStatus(email).then(state => {
        if (state) {
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
    const args = [
        name,
        email
    ];

    userModel.updateName(args).then(state => {
        if (state) {
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
    const args = [
        username,
        email
    ];

    userModel.updateUsername(args).then(state => {
        if (state) {
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
    const args = [
        newMail,
        email
    ];

    userModel.updateEmail(args).then(state => {
        if (state) {
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

    userModel.getUserPassword(email).then(result => {
        const state = result[0];
        if (state) {
            const oldPassword = result[1][0]['password'];
            userModel.comparePassword(password, oldPassword).then(isSamePassword => {
                if (isSamePassword) {
                    res.status(status.BAD_REQUEST).json({
                        message: "New password is equal old one"
                    });
                } else {
                    userModel.hashPassword(password).then(hashedPassword => {
                        const args = [
                            hashedPassword,
                            email
                        ];
                        userModel.updatePassword(args).then(state => {
                            if (state) {
                                res.status(status.OK).json({
                                    message: "Password changed",
                                });
                            }
                        });
                    })
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
    const args = [
        address,
        email
    ];

    userModel.updateAddress(args).then(state => {
        if (state) {
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
    const args = [
        status,
        email
    ];

    userModel.updateStatus(args).then(state => {
        if (state) {
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
    const args = [
        active,
        email
    ];

    userModel.updateActive(args).then(state => {
        if (state) {
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
        console.log("Email : " + email);

        userModel.getUserAvatar(email).then(oldAvatar => {
            if (oldAvatar != undefined || oldAvatar !== "null") {
                try {
                    fileSystem.unlinkSync(oldAvatar);
                } catch (err) {
                    console.error("Can't find file in storage/pictures Path");
                }
            }

            const args = [avatarPath, email];

            userModel.updateUserAvatar(args).then(state => {
                if (state) {
                    res.status(status.OK).json({
                        message: "Avatar Updated",
                    });
                } else {
                    res.status(status.BAD_REQUEST).json({
                        message: "Can't update Avatar"
                    });
                }
            })
        });
    }
}