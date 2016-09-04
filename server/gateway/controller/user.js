var co = require('co');
var jwt = require('jsonwebtoken');
var config = require('config');

var logger = require('../logger');
var User = require('../models/user');

exports.login = function (req, res, next) {
    var loginData = req.body;
    User.findOne({ id: loginData.id }, function (err, user) {
        if (err) {
            logger.error(err);
            res.status(401).send();
            return;
        }

        if (user.password === loginData.password) {
            var token = jwt.sign({},  // payload
                config.secretKey,     // secret key
                {                     // options
                    subject: user.id,
                    issuer: config.issuerStr
                });

            res.json({
                token : token
            });
        }
        else {
            res.status(401).send();
        }
    });
};

exports.register = function(req, res, next) {
    var profile = req.body;
    logger.info('register : ' + profile);
    co(function *() {
        var userExists = yield User.findOne({id : profile.id}).exec();
        if (userExists) {
            var msg = 'User is already existed. (' + profile.id + ')';
            res.status(400).json({
                message : msg
            })
            return;
        }

        var user = new User(profile);
        yield user.save();
        res.status(200).json(user);
    }).catch(next);    
};

exports.getUser = function(req, res, next) {
    User.find({}, function(err, users) {
        if(err) {
            logger.error(err);
            res.status(500).send();
            return;
        }
        res.json(users);
        return;
    });
};
