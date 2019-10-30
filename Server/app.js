const express = require('express');
const app = express();

const userRoutes = require('./api/routes/user');
const API_VERSION = "/v1/";

app.use(API_VERSION + 'users', userRoutes);

module.exports = app;