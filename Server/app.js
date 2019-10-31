const express = require('express');
const app = express();

const API_VERSION = "/v1/";
const userRoutes = require('./api/routes/user');
const questionRoutes = require('./api/routes/question');
const answerRoutes = require('./api/routes/answer');
const followRoutes = require('./api/routes/follow');
const notificationRoutes = require('./api/routes/notification');

app.use(API_VERSION + 'users', userRoutes);
app.use(API_VERSION + 'questions', questionRoutes);
app.use(API_VERSION + 'answers', answerRoutes);
app.use(API_VERSION + 'follows', followRoutes);
app.use(API_VERSION + 'notifications', notificationRoutes);

module.exports = app;