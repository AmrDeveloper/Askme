const express = require('express');
const databse = require('../../database/config')
const router = express.Router();

router.get('/', (req, res) => {
    res.status(200).json({
        message: "GET request on api/users"
    })
});

module.exports = router;