const express = require('express');
const app = express();

const userRoutes = require('./api/routes/user');

app.use('/api/users',userRoutes);

module.exports = app;