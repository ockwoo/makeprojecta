var mongoose = require('mongoose');
var config = require('config');
var logger = require('../../logger');

logger.info('connecting db : ' + config.mongoose.ADOPTION_DB_URL);
var db = mongoose.createConnection(config.mongoose.ADOPTION_DB_URL);

var adoptionSchema = new mongoose.Schema({
    title : {
        type : String
    },
    picture : {
        type : [String]
    },
    price : {
        type : String
    },
    user : {
        type : String
    },
    pet : {
        type : mongoose.Schema.ObjectId,
        ref : 'Pet',
        require : true,
        index : true
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

var Adoption = db.model('Adoption', adoptionSchema);
module.exports = Adoption;