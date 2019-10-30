const databse = require('../../database/config')

const QUERY_DEFAULT_OFFSET = 0;
const QUERY_DEFAULT_COUNT = 25;
const QUERY_MAX_COUNT = 50;

exports.getAllUsers = (req, res) => {
    res.status(200).json({
        message: "GET All users"
    })
};

exports.getOneUser = (req, res) => {
    res.status(200).json({
        message: "GET One user by id"
    })
};

exports.registerNewUser = (req, res) => {
    res.status(200).json({
        message: "POST one user to database"
    })
};

exports.deleteAllUsers = (req, res) => {
    res.status(200).json({
        message: "Delete All users"
    })
};

exports.deleteOneUser = (req, res) => {
    res.status(200).json({
        message: "Delete One user by id"
    })
};

exports.updateName = (req,res) => {
    res.status(200).json({
        message: "Update name"
    })
};

exports.updateUsername = (req, res) => {
    res.status(200).json({
        message: "Update username"
    })
};

exports.updateEmail = (req, res) => {
    res.status(200).json({
        message: "Update user email"
    })
};

exports.updatePassword = (req, res) => {
    res.status(200).json({
        message: "Update user password"
    })
};

exports.updateAddress = (req, res) => {
    res.status(200).json({
        message: "Update user Address"
    })
}

exports.updateStatus = (req,res) => {
    res.status(200).json({
        message: "Update user status"
    })
};

exports.updateActive = (req,res) => {
    res.status(200).json({
        message: "Update user Active"
    })
};