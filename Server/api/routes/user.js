const express = require('express');
const router = express.Router();

router.get('/', (req, res) => {
    res.status(300).json({
        message: "GET request on api/users"
    })
});

module.exports = router;