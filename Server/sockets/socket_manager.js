const stateModel = require('../api/models/state');

//Events 
const CONNECTED = 'connection';
const DIS_CONNECTED = 'disconnect';
const INFORMATION = 'information';

exports.startListener = (socketIO) => {
    socketIO.on(CONNECTED, socket => {
        var userId = 0;
        socket.on(INFORMATION, id => {
            userId = id;
            stateModel.makeUserOnline(userId);
        });
        socket.on(DIS_CONNECTED, () => {
            stateModel.makeUserOffline(userId);
        });
    });
};