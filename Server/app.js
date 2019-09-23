const express = require('express');
const app = express();

app.use((req, res, next) => {
    res.status(300).json({
        message : "Hellp, World!"
    })
});

module.exports = app;