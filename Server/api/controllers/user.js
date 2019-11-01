const databse = require('../../database/config');
const bcrypt = require('bcrypt');

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
    res.status(200).json({
        message: "User Login"
    });
};

exports.registerNewUser = (req, res) => {
    const email = req.query.email;
    const password = req.query.password;
    bcrypt.hash(password, 10, (err, hash) => {
        if (err) {
            return res.status(500).json({
                error: err
            })
        } else {
            //Register User
            console.log(hash);
            res.status(200).json({
                message: "POST one user to database"
            });
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
    res.status(200).json({
        message: "Update user password"
    });
};

exports.updateAddress = (req, res) => {
    res.status(200).json({
        message: "Update user Address"
    });
}

exports.updateStatus = (req, res) => {
    res.status(200).json({
        message: "Update user status"
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