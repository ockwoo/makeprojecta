var formidable = require('formidable');
var config = require('config');
var AWS = require('aws-sdk');
AWS.config.update({
    accessKeyId: config.storage.ki,
    secretAccessKey: config.storage.sk,
    region : 'ap-northeast-2',
});

var Pet = require('../models/pet/pet');
var Adop = require('../models/pet/adoption');
var logger = require('../logger');
//////////////////////////////////////////////////////////////////////////
// Pet
//////////////////////////////////////////////////////////////////////////
/**
 * 
 */
exports.getPet = function(req, res, next) {
    var id = req.params.id;
    if (id) {
        Pet.find({ senderId: id }, function (e, pets) {
            res.json(pets);
        });
    }
    else {
        Pet.find({}, function (e, pets) {
        	res.json(pets);
        });
    }
};

exports.addPet = function (req, res, next) {
    var body = req.body;
    //console.log('body : ' + JSON.stringify(body));
    var newPet = Pet(body);

    newPet.save(function(e) {
        if(e) throw err;
        res.json(body);
    });
};

exports.deletePet = function(req, res, next) {
    var id = req.params.id;
    if(!id) {
        return res.status(400).send();
    }

    Pet.remove( { _id : id }, function(err) {
        if(err) {
            return res.status(500).send({
                message : err
            });
        }
        else {
            return res.status(200).send({
                message : "ok"
            });
        }
    });
};


//////////////////////////////////////////////////////////////////////////
// Adoption
//////////////////////////////////////////////////////////////////////////

exports.getAdop = function(req, res, next) {
    var id = req.params.id;
    if (id) {
        Adop.findById(id , function (e, pets) {
            res.json(pets);
        });
    }
    else {
        Adop.find({}, function (e, pets) {
        	res.json(pets);
        });
    }
};

exports.addAdop = function (req, res, next) {
    var body = req.body;
    //console.log('body : ' + JSON.stringify(body));
    var newAdop = Adop(body);

    newAdop.save(function(e) {
        if(e) throw err;
        res.json(body);
    });
};

exports.deleteAdop = function(req, res, next) {
    var id = req.params.id;
    if(!id) {
        return res.status(400).send();
    }

    Adop.remove( { _id : id }, function(err) {
        if(err) {
            return res.status(500).send({
                message : err
            });
        }
        else {
            return res.status(200).send({
                message : "ok"
            });
        }
    });
};


function upload (req, res) {

}


exports.uploadPicture = function(req, res, next) {
    var form = new formidable.IncomingForm();
    form.parse(req, function(err, field, files) {
        var s3 = new AWS.S3();
        var param = {
            Bucket : config.storage.bn,
            Key : files.userfile.name,
            ACL : 'public-read',
            Body : require('fs').createReadStream(files.userfile.path),
            //ContentType: files.mimetype
        };
        s3.upload(param, function(err, data){
            var result='';
            if(err) {
                logger.error(err);
                res.status(500).send({
                    message : err
                });
            }
            else {
                //
                if (req.params.id) {
                    Adop.findById(req.params.id, function (e, adop) {
                        //res.json(pets);
                        if(e) {
                            logger.error(e);
                            return;
                        }
                        if(adop) {
                           adop.picture.unshift(data.Location);
                           adop.save();
                        }
                    });
                }
                res.status(200).send({
                    message : "ok",
                    data : data.Location
                });
            }
        });
    });   
};


