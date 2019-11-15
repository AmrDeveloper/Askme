const http = require('http');
const app = require('./app');

const port = process.env.PORT || 3000;

const server = http.createServer(app);

const socketIO = require('socket.io')(server);

const socketManager = require('./sockets/socket_manager');
socketManager.startListener(socketIO);

server.listen(port, () => console.debug("Server start at PORT 3000"));