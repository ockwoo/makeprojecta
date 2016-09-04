var mongoose = require('mongoose');
var config = require('config');
var logger = require('../../logger');

logger.info('connecting db : ' + config.mongoose.PET_DB_URL);
var db = mongoose.createConnection(config.mongoose.PET_DB_URL);

var petSchema = new mongoose.Schema({
    category : {
        type : String
    },
    kind : {
        type : String
    },
    age : {
        type : Number
    },
    remarks : {
        type : String
    },
    crteTm : {
        type: Date,
        default: Date.now
    },
    modyTm : {
        type: Date,
        default: Date.now
    }
});

var Pet = db.model('Pet', petSchema);
module.exports = Pet;