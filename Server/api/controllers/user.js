//const databse = require('../../database/config')

let QUERY_DEFAULT_OFFSET = 0;
let QUERY_DEFAULT_COUNT = 25;
let QUERY_MAX_COUNT = 50;

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