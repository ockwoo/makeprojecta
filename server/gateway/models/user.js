var mongoose = require('mongoose');
var validator = require('validator');
var config = require('config');
var logger = require('../logger');

logger.info('connecting db : ' + config.mongoose.USER_DB_URL);
var db = mongoose.createConnection(config.mongoose.USER_DB_URL);
// TODO : catch error

var userSchema = new mongoose.Schema({
    /* id is email address. */
    id : { 
        type : String,
        require : true,
        unique : true,
    },
    password : {
        type : String, 
        required : true
    },
    phone : {
        type : String
    }, 
    /* src indicate how to register */
    src : {
        type : String    
    },
    profileUrl : {
        type : String
    },
    roles : {
        type : [ String ]
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

userSchema.pre('save', function(next) {
    if(!this.isModified('password')) {
        return next();
    }
    //this.password = User.encryptPassword(this.password);
    //this.password = User.encryptPassword(this.password);
    next();
});


var User = db.model('User', userSchema);

User.schema.path('id').validate(function(email) {
    validator.isEmail(email);
});

module.exports = User;