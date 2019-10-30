const express = require('express');
const app = express();

const API_VERSION = "/v1/";
const userRoutes = require('./api/routes/user');
const questionRoutes = require('./api/routes/question');

app.use(API_VERSION + 'users', userRoutes);
app.use(API_VERSION + 'questions', questionRoutes);

module.exports = app;